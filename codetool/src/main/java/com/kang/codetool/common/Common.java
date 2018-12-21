package com.kang.codetool.common;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.util.ClassUtil;
import db.KlDatabase;
import db.KlDatabaseType;
import db.KlFieldDescription;
import org.springframework.web.bind.annotation.RequestMapping;
import other.KlConvert;
import other.KlString;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Common {

    public static String[] getViewPage() {


        return null;
    }

    public static Map<String, Method> getAllPageMethod() {

        Set<Class<?>> clazzs = ClassUtil.getClasses("com.kang.codetool.controller");
        Map<String, Method> methodMap = new LinkedHashMap<>();
        for (Class<?> clazz : clazzs) {

            // 获取方法上的注解
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Annotation[] annotations = method.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().getSimpleName().equals("ViewPage")) {
                        methodMap.put(Common.getMethodDescription(method), method);
                    }
                }
            }
        }
        return methodMap;
    }


    public static String getUrlByControllerMethod(Method method) {
        String result = "";
        RequestMapping[] parentType = method.getDeclaringClass().getAnnotationsByType(RequestMapping.class);
        if (parentType.length > 0) {
            result += "/" + parentType[0].value()[0];
        }
        RequestMapping[] currentType = method.getAnnotationsByType(RequestMapping.class);
        if (currentType.length > 0) {
            result += "/" + currentType[0].value()[0];
        }

        return result;
    }

    public static String getMethodDescription(Method method) {
        ViewPage[] currentType = method.getAnnotationsByType(ViewPage.class);
        if (currentType.length > 0) {
            return currentType[0].description();
        }
        return "";
    }

    private static List<String> getClassName(String filePath, List<String> className) {
        List<String> myClassName = new ArrayList<String>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(getClassName(childFile.getPath(), myClassName));
            } else {
                String childFilePath = childFile.getPath();
                childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                childFilePath = childFilePath.replace("\\", ".");
                myClassName.add(childFilePath);
            }
        }

        return myClassName;
    }


    public static List<String> getDatabaseTables(String connectionString, KlDatabaseType dbType) {
        switch (dbType) {
            case MySql: {
                return getDatabaseTables_MySql(connectionString);
            }
            case SqlServer: {
                return getDatabaseTables_SqlServer(connectionString);
            }
            case PostgreSql: {
                return getDatabaseTables_PostgreSql(connectionString);
            }
            default:
                return null;
        }
    }

    private static List<String> getDatabaseTables_SqlServer(String connectionString) {
        String sql = "SELECT name AS Name FROM SYSOBJECTS WHERE XTYPE IN ('V','U') AND NAME<>'DTPROPERTIES' ORDER BY Name ASC";

        List<Map<String, Object>> result = null;
        try {
            result = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.stream().map(r -> r.get("name").toString()).collect(Collectors.toList());
    }

    private static List<String> getDatabaseTables_MySql(String connectionString) {
        String dbName = connectionString.split("database=")[1].split(";")[0];
        String sql = "SELECT TABLE_NAME Name FROM information_schema.TABLES WHERE TABLE_SCHEMA = '" + dbName + "' AND TABLE_TYPE = 'BASE TABLE'";

        List<Map<String, Object>> result = null;
        try {
            result = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.stream().map(r -> r.get("name").toString()).collect(Collectors.toList());
    }

    private static List<String> getDatabaseTables_PostgreSql(String connectionString) {
        String sql = "select relname as Name,cast(obj_description(relfilenode,'pg_class') as varchar) as comment from pg_class c \n" +
                "where  relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname";

        List<Map<String, Object>> result = null;
        try {
            result = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.stream().map(r -> r.get("name").toString()).collect(Collectors.toList());
    }




    public static List<KlFieldDescription> getDatabaseColumns(String connectionString, String tableName, KlDatabaseType dbType)
    {
        switch (dbType)
        {
            case MySql:
            {
                return GetDatabaseColumns_MySql(connectionString, tableName);
            }
            case SqlServer:
            {
                return GetDatabaseColumns_SqlServer(connectionString, tableName);
            }
            case PostgreSql:
            {
                return GetDatabaseColumns_PostgreSql(connectionString, tableName);
            }
            default: return null;
        }
    }

    public static List<KlFieldDescription> GetDatabaseColumns_SqlServer(String connectionString, String tableName) {
        String sql =
                "        SELECT" +
                        "                Name=C.name," +
                        "                DbType=T.name," +
                        "                PrimaryKey=ISNULL(IDX.PrimaryKey,N'')," +
                        "        IsIdentity=CASE WHEN C.is_identity=1 THEN N'true'ELSE N'false' END," +
                        "            Length=C.max_length," +
                        "            IsNullable=CASE WHEN C.is_nullable=1 THEN N'true'ELSE N'false' END," +
                        "            Description=ISNULL(PFD.[value],N'')" +
                        "        FROM sys.columns C" +
                        "        INNER JOIN sys.objects O" +
                        "        ON C.[object_id]=O.[object_id]" +
                        "       AND O.type='U'" +
                        "        AND O.is_ms_shipped=0" +
                        "        INNER JOIN sys.types T" +
                        "        ON C.user_type_id=T.user_type_id" +
                        "        LEFT JOIN sys.default_constraints D" +
                        "        ON C.[object_id]=D.parent_object_id" +
                        "        AND C.column_id=D.parent_column_id" +
                        "        AND C.default_object_id=D.[object_id]" +
                        "        LEFT JOIN sys.extended_properties PFD" +
                        "        ON PFD.class=1" +
                        "        AND C.[object_id]=PFD.major_id" +
                        "        AND C.column_id=PFD.minor_id" +
                        "            --             AND PFD.name='Caption'  -- 字段说明对应的描述名称(一个字段可以添加多个不同name的描述)" +
                        "        LEFT JOIN sys.extended_properties PTB" +
                        "        ON PTB.class=1" +
                        "        AND PTB.minor_id=0" +
                        "        AND C.[object_id]=PTB.major_id" +
                        "            --             AND PFD.name='Caption'  -- 表说明对应的描述名称(一个表可以添加多个不同name的描述)" +
                        "        LEFT JOIN                       -- 索引及主键信息" +
                        "            (" +
                        "                    SELECT" +
                        "                    IDXC.[object_id]," +
                        "            IDXC.column_id," +
                        "            Sort=CASE INDEXKEY_PROPERTY(IDXC.[object_id],IDXC.index_id,IDXC.index_column_id,'IsDescending')" +
                        "        WHEN 1 THEN 'DESC' WHEN 0 THEN 'ASC' ELSE '' END," +
                        "            PrimaryKey=CASE WHEN IDX.is_primary_key=1 THEN N'PRI'ELSE N'' END," +
                        "            IndexName=IDX.Name" +
                        "        FROM sys.indexes IDX" +
                        "        INNER JOIN sys.index_columns IDXC" +
                        "        ON IDX.[object_id]=IDXC.[object_id]" +
                        "        AND IDX.index_id=IDXC.index_id" +
                        "        LEFT JOIN sys.key_constraints KC" +
                        "        ON IDX.[object_id]=KC.[parent_object_id]" +
                        "        AND IDX.index_id=KC.unique_index_id" +
                        "        INNER JOIN  -- 对于一个列包含多个索引的情况,只显示第1个索引信息" +
                        "            (" +
                        "                    SELECT [object_id], Column_id, index_id=MIN(index_id)" +
                        "                    FROM sys.index_columns" +
                        "                    GROUP BY [object_id], Column_id" +
                        "            ) IDXCUQ" +
                        "        ON IDXC.[object_id]=IDXCUQ.[object_id]" +
                        "        AND IDXC.Column_id=IDXCUQ.Column_id" +
                        "        AND IDXC.index_id=IDXCUQ.index_id" +
                        "    ) IDX" +
                        "        ON C.[object_id]=IDX.[object_id]" +
                        "        AND C.column_id=IDX.column_id" +
                        "       where O.name = '" + tableName + "'" +
                        "       order by c.column_id";

        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<KlFieldDescription> result = new ArrayList<>();
        for (Map<String, Object> map : queryResult) {
            KlFieldDescription model = new KlFieldDescription();
            model.setName(KlString.replaceUnderline(map.get("name").toString()));
            model.setDbType(map.get("dbtype").toString());
            model.setLength(KlConvert.tryToInteger(map.get("length")));
            model.setIsNullable(KlConvert.tryToBoolean(map.get("isnullable").toString()));
            model.setIsIdentity(KlConvert.tryToBoolean(map.get("isidentity").toString()));
            model.setDescription(KlConvert.tryToString(map.get("description")));
            model.setColumnKey(map.get("primarykey").toString());
            result.add(model);
        }

        return result;
    }

    public static List<KlFieldDescription> GetDatabaseColumns_PostgreSql(String connectionString, String tableName) {
        String sql =
                "select ordinal_position as Colorder,column_name as Name,data_type as DbType,\n" +
                        "coalesce(character_maximum_length,numeric_precision,-1) as Length,numeric_scale as Scale,\n" +
                        "case is_nullable when 'NO' then 0 else 1 end as IsNullable,column_default as DefaultVal,\n" +
                        "case  when position('nextval' in column_default)>0 then 1 else 0 end as IsIdentity, \n" +
                        "case when b.pk_name is null then 0 else 1 end as IsPK,c.DeText Description\n" +
                        "from information_schema.columns \n" +
                        "left join (\n" +
                        "    select pg_attr.attname as colname,pg_constraint.conname as pk_name from pg_constraint  \n" +
                        "    inner join pg_class on pg_constraint.conrelid = pg_class.oid \n" +
                        "    inner join pg_attribute pg_attr on pg_attr.attrelid = pg_class.oid and  pg_attr.attnum = pg_constraint.conkey[1] \n" +
                        "    inner join pg_type on pg_type.oid = pg_attr.atttypid\n" +
                        "    where pg_class.relname = '" + tableName + "' and pg_constraint.contype='p' \n" +
                        ") b on b.colname = information_schema.columns.column_name\n" +
                        "left join (\n" +
                        "    select attname,description as DeText from pg_class\n" +
                        "    left join pg_attribute pg_attr on pg_attr.attrelid= pg_class.oid\n" +
                        "    left join pg_description pg_desc on pg_desc.objoid = pg_attr.attrelid and pg_desc.objsubid=pg_attr.attnum\n" +
                        "    where pg_attr.attnum>0 and pg_attr.attrelid=pg_class.oid and pg_class.relname='" + tableName + "'\n" +
                        ")c on c.attname = information_schema.columns.column_name\n" +
                        "where table_schema='public' and table_name='" + tableName + "' order by ispk desc,isidentity desc,ordinal_position asc";

        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<KlFieldDescription> result = new ArrayList<>();
        for (Map<String, Object> map : queryResult) {
            KlFieldDescription model = new KlFieldDescription();
            model.setName(KlString.replaceUnderline(map.get("name").toString()));
            model.setDbType(map.get("dbtype").toString());
            model.setLength(KlConvert.tryToInteger(map.get("length")));
            model.setIsNullable(KlConvert.tryToBoolean("1".equals(map.get("isnullable").toString())));
            model.setIsIdentity(KlConvert.tryToBoolean("1".equals(map.get("isidentity").toString())));
            model.setDescription(KlConvert.tryToString(map.get("description")));
            model.setColumnKey("1".equals(map.get("ispk").toString())?"PRI":"");
            result.add(model);
        }

        return result;
    }

    public static List<KlFieldDescription> GetDatabaseColumns_MySql(String connectionString, String tableName) {
        String dbName = connectionString.split("database=")[1].split(";")[0];
        String sql =
                "SELECT COLUMN_NAME name, COLUMN_COMMENT description, DATA_TYPE DbType, IS_NULLABLE isNullable, CHARACTER_MAXIMUM_LENGTH length, Extra, column_key\n" +
                        "FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = '" + dbName + "' AND TABLE_NAME like '" + tableName + "'";

        List<Map<String, Object>> queryResult = null;
        try {
            queryResult = KlDatabase.fill(connectionString, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<KlFieldDescription> result = new ArrayList<>();
        for (Map<String, Object> map : queryResult) {
            KlFieldDescription model = new KlFieldDescription();
            model.setName(KlString.replaceUnderline(map.get("name").toString()));
            model.setDbType(map.get("dbtype").toString());
            model.setLength(KlConvert.tryToInteger(map.get("length")));
            model.setIsNullable(KlConvert.tryToBoolean("yes".equals(map.get("isnullable").toString().toLowerCase())));
            model.setIsIdentity(KlConvert.tryToBoolean(map.get("extra").toString().contains("auto_increment")));
            model.setDescription(KlConvert.tryToString(map.get("description")));
            model.setColumnKey(map.get("column_key").toString());
            result.add(model);
        }

        return result;
    }
}
