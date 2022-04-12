package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.lab.utils.vo.RestResponse;
import com.kang.lab.utils.db.DatabaseUtil;
import com.kang.lab.utils.enums.DatabaseTypeEnum;
import com.kang.lab.utils.db.FieldDescriptionUtil;
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
    public RestResponse<List<Map<String, Object>>> getDbFile(String connection, String username, String password, String dbType) {
        connection = URLDecoder.decode(connection);
        username = URLDecoder.decode(username.trim());
        password = URLDecoder.decode(password.trim());
        List<Map<String, Object>> result = new CopyOnWriteArrayList<>();

        try {
            List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connection, username, password, DatabaseTypeEnum.getByName(dbType));
            List<FieldDescriptionUtil> columnList = Common.getDatabaseColumns(connection, username, password, null, DatabaseTypeEnum.getByName(dbType));

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
            return RestResponse.success(result);
        } catch (Exception ex) {
            log.error("打印数据库文档失败", ex);
            return RestResponse.error("打印数据库文档失败:" + ex.getMessage());
        }
    }

    @PostMapping("getDbData")
    public RestResponse<List<Map<String, Object>>> getDbData(String connection, String username, String password, String dbType, String tableName) {
        connection = URLDecoder.decode(connection);
        username = URLDecoder.decode(username.trim());
        password = URLDecoder.decode(password.trim());
        if (connection.indexOf("zeroDateTimeBehavior") == -1) {
            connection += "&zeroDateTimeBehavior=CONVERT_TO_NULL";
        }
        List<Map<String, Object>> result = new CopyOnWriteArrayList<>();

        try {
            String sql = "";
            if (DatabaseTypeEnum.MySql.name().equals(dbType) || DatabaseTypeEnum.PostgreSql.name().equals(dbType)) {
                sql = "SELECT * FROM " + tableName + " LIMIT 5";
            } else if (DatabaseTypeEnum.SqlServer.name().equals(dbType)) {
                sql = "SELECT TOP 5 * FROM " + tableName;
            } else {
                return RestResponse.error("不支持该数据库");
            }
            List<Map<String, Object>> dataList = DatabaseUtil.fill(connection, username, password, sql);

            return RestResponse.success(dataList);
        } catch (Exception ex) {
            log.error("打印数据库文档失败", ex);
            return RestResponse.error("打印数据库文档失败:" + ex.getMessage());
        }
    }
}
