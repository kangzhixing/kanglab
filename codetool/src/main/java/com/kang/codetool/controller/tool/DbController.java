package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlResponse;
import com.kang.framework.db.KlDatabase;
import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
            List<KlFieldDescription> columnList = Common.getDatabaseColumns(connection, null, KlDatabaseType.getByName(dbType));

            for (Map<String, Object> dbTable : databaseTables) {
                try {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("dbName", dbTable.get("TABLE_NAME").toString());
                    m.put("comment", dbTable.get("TABLE_COMMENT").toString());
                    m.put("fieldDescriptions", columnList.stream().filter(li -> li.getTableName().equals(dbTable.get("TABLE_NAME").toString())).collect(Collectors.toList()));
                    result.add(m);
                } catch (Exception e) {
                    log.error("查询数据库失败", e);
                }
            }
            result.sort(Comparator.comparing(li -> li.get("dbName").toString()));
            return KlResponse.success(result);
        } catch (Exception ex) {
            log.error("打印数据库文档失败", ex);
            return KlResponse.fail("打印数据库文档失败:" + ex.getMessage());
        }
    }

    @PostMapping("getDbData")
    public KlResponse<List<Map<String, Object>>> getDbData(String connection, String dbType, String tableName) {
        connection = URLDecoder.decode(connection);
        if (connection.indexOf("zeroDateTimeBehavior") == -1) {
            connection += "&zeroDateTimeBehavior=CONVERT_TO_NULL";
        }
        List<Map<String, Object>> result = new CopyOnWriteArrayList<>();

        try {
            String sql = "";
            if (KlDatabaseType.MySql.name().equals(dbType) || KlDatabaseType.PostgreSql.name().equals(dbType)) {
                sql = "SELECT * FROM " + tableName + " LIMIT 5";
            } else if (KlDatabaseType.SqlServer.name().equals(dbType)) {
                sql = "SELECT TOP 5 * FROM " + tableName;
            } else {
                return KlResponse.fail("不支持该数据库");
            }
            List<Map<String, Object>> dataList = KlDatabase.fill(connection, sql);

            return KlResponse.success(dataList);
        } catch (Exception ex) {
            log.error("打印数据库文档失败", ex);
            return KlResponse.fail("打印数据库文档失败:" + ex.getMessage());
        }
    }
}
