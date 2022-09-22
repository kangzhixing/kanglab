package com.kang.codetool.controller.tool;

import com.alibaba.fastjson.JSON;
import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.controller.request.GetDbDataConditionReq;
import com.kang.codetool.controller.request.GetDbDataReq;
import com.kang.lab.utils.db.DatabaseUtil;
import com.kang.lab.utils.db.DbTypeMapUtil;
import com.kang.lab.utils.db.FieldDescriptionUtil;
import com.kang.lab.utils.enums.DatabaseTypeEnum;
import com.kang.lab.utils.exception.BizException;
import com.kang.lab.utils.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
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
    public RestResponse<List<Map<String, Object>>> getDbFile(String connection, String username, String password, String dbType, String tableName) {
        connection = URLDecoder.decode(connection);
        username = URLDecoder.decode(username.trim());
        password = URLDecoder.decode(password.trim());
        List<Map<String, Object>> result = new CopyOnWriteArrayList<>();

        try {
            List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connection, username, password, DatabaseTypeEnum.getByName(dbType));
            List<FieldDescriptionUtil> columnList = Common.getDatabaseColumns(connection, username, password, tableName, DatabaseTypeEnum.getByName(dbType));

            for (Map<String, Object> dbTable : databaseTables) {
                if (StringUtils.isNotBlank(tableName) && !tableName.equals(dbTable.get("TABLE_NAME").toString())) {
                    continue;
                }
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
    public RestResponse<List<Map<String, Object>>> getDbData(GetDbDataReq req) throws Exception {
        String connection = URLDecoder.decode(req.getConnection());
        String username = URLDecoder.decode(req.getUsername().trim());
        String password = URLDecoder.decode(req.getPassword().trim());
        String tableName = URLDecoder.decode(req.getTableName().trim());
        if (connection.indexOf("zeroDateTimeBehavior") == -1) {
            if (connection.indexOf("?") > -1) {
                connection += "&zeroDateTimeBehavior=CONVERT_TO_NULL";
            } else {
                connection += "?zeroDateTimeBehavior=CONVERT_TO_NULL";
            }
        }
        String sql = getSearchSql(connection, username, password, DatabaseTypeEnum.getByName(req.getDbType()), tableName, JSON.parseArray(req.getCondition(), GetDbDataConditionReq.class),
                req.getPageNum() == null ? 0 : req.getPageNum(), req.getPageSize() == null ? 10 : req.getPageSize());
        log.info("查询sql为: {}", sql);
        try {
            List<Map<String, Object>> dataList = DatabaseUtil.fill(connection, username, password, sql);

            return RestResponse.success(dataList);
        } catch (Exception ex) {
            log.error("查询数据库文档失败", ex);
            return RestResponse.error("查询数据库文档失败:" + ex.getMessage());
        }
    }

    @PostMapping("editData")
    public RestResponse editData(GetDbDataReq req) throws Exception {
        String connection = URLDecoder.decode(req.getConnection(), "UTF-8");
        String username = URLDecoder.decode(req.getUsername(), "UTF-8");
        String password = URLDecoder.decode(req.getPassword(), "UTF-8");
        String tableName = req.getTableName();

        if (connection.indexOf("zeroDateTimeBehavior") == -1) {
            connection += "&zeroDateTimeBehavior=CONVERT_TO_NULL";
        }
        String sql = getEditSql(connection, username, password, DatabaseTypeEnum.getByName(req.getDbType()), tableName, JSON.parseArray(req.getCondition(), GetDbDataConditionReq.class));
        log.info("修改sql为: {}", sql);
        try {
            int count = DatabaseUtil.executeNonQuery(connection, username, password, sql);

            return RestResponse.success(count);
        } catch (Exception ex) {
            log.error("修改数据失败", ex);
            return RestResponse.error("修改数据失败:" + ex.getMessage());
        }
    }

    @PostMapping("addData")
    public RestResponse addData(GetDbDataReq req) throws Exception {
        String connection = URLDecoder.decode(req.getConnection(), "UTF-8");
        String username = URLDecoder.decode(req.getUsername(), "UTF-8");
        String password = URLDecoder.decode(req.getPassword(), "UTF-8");
        String tableName = req.getTableName();

        if (connection.indexOf("zeroDateTimeBehavior") == -1) {
            connection += "&zeroDateTimeBehavior=CONVERT_TO_NULL";
        }
        String sql = getAddSql(connection, username, password, DatabaseTypeEnum.getByName(req.getDbType()), tableName, JSON.parseArray(req.getCondition(), GetDbDataConditionReq.class));
        try {
            int count = DatabaseUtil.executeNonQuery(connection, username, password, sql);

            return RestResponse.success(count);
        } catch (Exception ex) {
            log.error("添加数据失败", ex);
            return RestResponse.error("添加数据失败:" + ex.getMessage());
        }
    }

    private String getSearchSql(String connection, String username, String password, DatabaseTypeEnum dbType, String tableName, List<GetDbDataConditionReq> condition, int pageNum, int pageSize) throws Exception {
        List<FieldDescriptionUtil> columnList = Common.getDatabaseColumns(connection, username, password, tableName, dbType);

        String where = "1 = 1";
        if (CollectionUtils.isNotEmpty(condition)) {
            for (GetDbDataConditionReq con : condition) {
                if (con != null && StringUtils.isNotBlank(con.getValue())) {
                    String colTypeMysql = columnList.stream().filter(li -> con.getName().equals(li.getName())).findFirst().get().getDbType();
                    String colTypeJava = DbTypeMapUtil.map4J(colTypeMysql, true);
                    if ("string".equalsIgnoreCase(colTypeJava) || "date".equalsIgnoreCase(colTypeJava)) {
                        where += " AND " + con.getName() + " = '" + con.getValue() + "'";
                    } else {
                        where += " AND " + con.getName() + " = " + con.getValue();
                    }
                }
            }
        }
        switch (dbType) {
            case MySql:
            case PostgreSql:
                return String.format("SELECT * FROM %s WHERE %s LIMIT %s, %s", tableName, where, pageNum * pageSize, pageSize);
            case SqlServer:
                return String.format("SELECT * FROM %s WHERE %s offset %s rows fetch next %s rows only", tableName, where, pageNum * pageSize, pageSize);
            default:
                throw new BizException("不支持该数据库");
        }
    }

    private boolean isStringCondition(String colTypeJava) {
        return "string".equalsIgnoreCase(colTypeJava) || "date".equalsIgnoreCase(colTypeJava);
    }

    private String getEditSql(String connection, String username, String password, DatabaseTypeEnum dbType, String tableName, List<GetDbDataConditionReq> condition) throws Exception {
        if (CollectionUtils.isEmpty(condition)) {
            throw new BizException("条件不能为空");
        }
        if (!condition.stream().anyMatch(li -> StringUtils.isNotBlank(li.getValue()))) {
            throw new BizException("所有值均为空");
        }
        List<FieldDescriptionUtil> columnList = Common.getDatabaseColumns(connection, username, password, tableName, dbType);
        FieldDescriptionUtil pkField = columnList.stream().filter(li -> "PRI".equals(li.getColumnKey())).findFirst().orElseThrow(() -> new BizException("仅支持通过主键修改，该表没有设置主键"));
        GetDbDataConditionReq pkValue = condition.stream().filter(li -> StringUtils.isNotBlank(li.getValue()) && li.getName().equals(pkField.getName())).findFirst().orElseThrow(() -> new BizException("仅支持通过主键修改，主键不能为空"));
        // 拼where查询条件
        String whereSql = "WHERE " + pkField.getName() + " = ";
        if (isStringCondition(DbTypeMapUtil.map4J(pkField.getDbType(), true))) {
            whereSql += "'" + pkValue.getValue() + "'";
        } else {
            whereSql += pkValue.getValue();
        }
        //拼修改语句
        String setSql = "SET ";
        for (GetDbDataConditionReq con : condition) {
            if (con != null && StringUtils.isNotBlank(con.getValue()) && !pkField.getName().equals(con.getName())) {
                String colTypeMysql = columnList.stream().filter(li -> con.getName().equals(li.getName())).findFirst().get().getDbType();
                String colTypeJava = DbTypeMapUtil.map4J(colTypeMysql, true);
                if (isStringCondition(colTypeJava)) {
                    setSql += con.getName() + " = '" + con.getValue() + "',";
                } else {
                    setSql += con.getName() + " = " + con.getValue() + ",";
                }
            }
        }
        setSql = setSql.substring(0, setSql.length() - 1);

        switch (dbType) {
            case MySql:
            case PostgreSql:
                return String.format("UPDATE %s %s %s", tableName, setSql, whereSql);
            case SqlServer:
            default:
                throw new BizException("不支持该数据库");
        }
    }

    private String getAddSql(String connection, String username, String password, DatabaseTypeEnum dbType, String tableName, List<GetDbDataConditionReq> condition) throws Exception {
        if (CollectionUtils.isEmpty(condition)) {
            throw new BizException("条件不能为空");
        }
        if (!condition.stream().anyMatch(li -> StringUtils.isNotBlank(li.getValue()))) {
            throw new BizException("所有值均为空");
        }
        List<FieldDescriptionUtil> columnList = Common.getDatabaseColumns(connection, username, password, tableName, dbType);

        //拼修改语句
        String colSql = "";
        String valueSql = "";
        for (GetDbDataConditionReq con : condition) {
            if (con != null && StringUtils.isNotBlank(con.getValue())) {
                String colTypeMysql = columnList.stream().filter(li -> con.getName().equals(li.getName())).findFirst().get().getDbType();
                String colTypeJava = DbTypeMapUtil.map4J(colTypeMysql, true);
                colSql += con.getName() + ",";
                if (isStringCondition(colTypeJava)) {
                    valueSql += "'" + con.getValue() + "',";
                } else {
                    valueSql += con.getValue() + ",";
                }
            }
        }
        colSql = colSql.substring(0, colSql.length() - 1);
        valueSql = valueSql.substring(0, valueSql.length() - 1);

        switch (dbType) {
            case MySql:
            case PostgreSql:
                return String.format("INSERT INTO %s (%s) values (%s)", tableName, colSql, valueSql);
            case SqlServer:
            default:
                throw new BizException("不支持该数据库");
        }
    }
}
