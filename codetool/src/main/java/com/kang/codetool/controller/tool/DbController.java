package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlRequest;
import com.kang.framework.db.KlDatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public KlRequest<List<Map<String, Object>>> getDbFile(String connection, String dbType) {
        KlRequest<List<Map<String, Object>>> request = new KlRequest<>();
        connection = URLDecoder.decode(connection);
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connection, KlDatabaseType.getByName(dbType));
            String finalConnection = connection;
            databaseTables.forEach(dbTable ->
            {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("dbName", dbTable.get("TABLE_NAME").toString());
                m.put("comment", dbTable.get("TABLE_COMMENT").toString());
                m.put("fieldDescriptions", Common.getDatabaseColumns(finalConnection, dbTable.get("TABLE_NAME").toString(), KlDatabaseType.getByName(dbType)));
                result.add(m);
            });

            request.setBody(result);
            return request;
        } catch (Exception ex) {
            request.setCode(0);
            request.setMsg(ex.getMessage());
            log.info("打印数据库文档失败", ex);
            return request;
        }
    }

}
