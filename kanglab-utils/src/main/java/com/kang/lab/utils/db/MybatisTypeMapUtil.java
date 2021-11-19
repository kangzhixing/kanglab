package com.kang.lab.utils.db;

import com.kang.lab.utils.enums.DatabaseTypeEnum;
import org.apache.commons.lang3.StringUtils;

public class MybatisTypeMapUtil {

    private static final String mysqlKey = ",add,all,alter,analyze,and,as,asc,asensitive,before,between,bigint,binary,blob,both,by,call,cascade,case,change,char,character,check,collate,column,condition,connection,constraint,continue,convert,create,cross,current_date,current_time,current_timestamp,current_user,cursor,database,databases,day_hour,day_microsecond,day_minute,day_second,dec,decimal,declare,default,delayed,delete,desc,describe,deterministic,distinct,distinctrow,div,double,drop,dual,each,else,elseif,enclosed,escaped,exists,exit,explain,false,fetch,float,float4,float8,for,force,foreign,from,fulltext,goto,grant,group,having,high_priority,hour_microsecond,hour_minute,hour_second,if,ignore,in,index,infile,inner,inout,insensitive,insert,int,int1,int2,int3,int4,int8,integer,interval,into,is,iterate,join,key,keys,kill,label,leading,leave,left,like,limit,linear,lines,load,localtime,localtimestamp,lock,long,longblob,longtext,loop,low_priority,match,mediumblob,mediumint,mediumtext,middleint,minute_microsecond,minute_second,mod,modifies,natural,not,no_write_to_binlog,null,numeric,on,optimize,option,optionally,or,order,out,outer,outfile,precision,primary,procedure,purge,raid0,range,read,reads,real,references,regexp,release,rename,repeat,replace,require,restrict,return,revoke,right,rlike,schema,schemas,second_microsecond,select,sensitive,separator,set,show,smallint,spatial,specific,sql,sqlexception,sqlstate,sqlwarning,sql_big_result,sql_calc_found_rows,sql_small_result,ssl,starting,straight_join,table,terminated,then,tinyblob,tinyint,tinytext,to,trailing,trigger,true,undo,union,unique,unlock,unsigned,update,usage,use,using,utc_date,utc_time,utc_timestamp,values,varbinary,varchar,varcharacter,varying,when,where,while,with,write,x509,xor,year_month,zerofill,";

    /**
     * 映射
     *
     * @param dbType 数据类型
     * @return JAVA类型
     */
    public static String map4MybatisPostgreSql(String dbType) {
        switch (dbType) {
            case "bigint":
                return "BIGINT";
            case "smallint":
                return "SMALLINT";
            case "tinyint":
                return "TINYINT";
            case "integer":
                return "INTEGER";
            case "boolean":
                return "BIT";
            case "date":
                return "DATE";
            case "timestamp":
            case "timestamp with time zone":
            case "timestamp without time zone":
                return "TIMESTAMP";
            case "numeric":
                return "NUMERIC";
            case "real":
                return "FLOAT";
            case "double precision":
                return "DOUBLE";
            default:
                return "VARCHAR";
        }
    }

    /**
     * 映射
     *
     * @param dbType 数据类型
     * @return JAVA类型
     */
    public static String map4Mybatis(String dbType, DatabaseTypeEnum databaseType) {
        if(databaseType.equals(DatabaseTypeEnum.PostgreSql)){
            return map4MybatisPostgreSql(dbType);
        }else{
            return map4Mybatis(dbType);
        }


    }

    /**
     * 映射
     *
     * @param dbType 数据类型
     * @return JAVA类型
     */
    public static String map4Mybatis(String dbType) {
        switch (dbType) {
            case "bigint":
                return "bigint";
            case "bit":
                return "bit";
            case "int":
                return "integer";
            case "tinyint":
                return "tinyint";
            case "date":
                return "date";
            case "datetime":
            case "timestamp":
                return "timestamp";
            case "numeric":
                return "numeric";
            case "decimal":
                return "decimal";
            case "float":
            case "double":
                return "double";
            case "text":
            case "varchar":
                return "varchar";
            case "nvarchar":
                return "nvarchar";
            default:
                return "varchar";
        }
    }

    public static String getSafeParam(String key, DatabaseTypeEnum databaseType) {
        switch (databaseType) {
            case MySql: {
                if (StringUtils.isNotBlank(key) && mysqlKey.contains("," + key.toLowerCase() + ",")) {
                    return "`" + key + "`";
                } else {
                    return key;
                }
            }
            case PostgreSql:
                return key;
            default:
                return key;
        }

    }
}
