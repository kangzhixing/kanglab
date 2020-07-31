package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlResponse;
import com.kang.framework.db.KlDatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("db")
public class DbController {

    @ViewPage(description = "数据库文档")
    @RequestMapping("dbDoc")
    public ModelAndView dbDoc() {
        return new ModelAndView("tool/dbDoc");
    }

    @RequestMapping("getDbFile")
    @ResponseBody
    public KlResponse<List<Map<String, Object>>> getDbFile(String connection, String dbType) {
        connection = URLDecoder.decode(connection);
        List<Map<String, Object>> result = new CopyOnWriteArrayList<>();

        try {
            List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connection, KlDatabaseType.getByName(dbType));
            String finalConnection = connection;
            ExecutorService executor = new ThreadPoolExecutor(10, 150,
                    60L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>());
            CountDownLatch cdl = new CountDownLatch(databaseTables.size());
            for (Map<String, Object> dbTable : databaseTables) {
                executor.execute(() -> {
                    try {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("dbName", dbTable.get("TABLE_NAME").toString());
                        m.put("comment", dbTable.get("TABLE_COMMENT").toString());
                        m.put("fieldDescriptions", Common.getDatabaseColumns(finalConnection, dbTable.get("TABLE_NAME").toString(), KlDatabaseType.getByName(dbType)));
                        result.add(m);
                    } catch (Exception e) {
                        log.error("查询数据库失败", e);
                    }
                    cdl.countDown();
                });
            }
            cdl.await();
            result.sort(Comparator.comparing(li -> li.get("dbName").toString()));
            return KlResponse.success(result);
        } catch (Exception ex) {
            log.error("打印数据库文档失败", ex);
            return KlResponse.fail("打印数据库文档失败:" + ex.getMessage());
        }
    }
}
