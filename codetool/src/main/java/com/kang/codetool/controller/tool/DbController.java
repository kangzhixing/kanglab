package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlRequest;
import com.kang.framework.db.KlDatabaseType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<String> databaseTables = Common.getDatabaseTables(connection, KlDatabaseType.getByName(dbType));
        String finalConnection = connection;
        databaseTables.forEach(dbTableName ->
        {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("dbName", dbTableName);
            m.put("fieldDescriptions", Common.getDatabaseColumns(finalConnection, dbTableName, KlDatabaseType.getByName(dbType)));
            result.add(m);
        });

        request.setBody(result);
        return request;
    }

}
