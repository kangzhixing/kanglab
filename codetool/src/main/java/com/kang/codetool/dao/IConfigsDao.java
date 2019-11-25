package com.kang.codetool.dao;

import com.kang.codetool.model.Configs;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import java.util.List;
import java.util.Map;

public interface IConfigsDao{

    @Select("select id, type, `key`, value, `describe`, valid_flag as validFlag, create_time as createTime, " +
            "update_time as updateTime from t_configs where id = #{id}")
    Configs selectById(Long id);

    @Select("select id, type, `key`, value, `describe`, valid_flag as validFlag, create_time as createTime, " +
            "update_time as updateTime from t_configs")
    List<Configs> selectAll();

    @SelectProvider(type = ConfigsProvider.class, method = "selectByPage")
    List<Configs> selectByPage(Configs configs);

    @SelectProvider(type = ConfigsProvider.class, method = "selectByWhere")
    Configs selectByWhere(Configs configs);

    @SelectProvider(type = ConfigsProvider.class, method = "selectListByWhere")
    List<Configs> selectListByWhere(Configs configs);

    @SelectProvider(type = ConfigsProvider.class, method = "count")
    int count(Configs configs);

    @Delete("delete from t_configs where id=#{id}")
    int deleteById(Long id);

    @UpdateProvider(type = ConfigsProvider.class, method = "updateById")
    int updateById(Configs configs);

    @UpdateProvider(type = ConfigsProvider.class, method = "updateByIdSelective")
    int updateByIdSelective(Configs configs);

    @InsertProvider(type = ConfigsProvider.class, method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Configs configs);

    @InsertProvider(type = ConfigsProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertSelective(Configs configs);

    @InsertProvider(type = ConfigsProvider.class, method = "batchInsert")
    void batchInsert(@Param("list") List<Configs> configsList);

    class ConfigsProvider extends SQL {

        private String getSqlWhere(Configs configs) {
            StringBuilder where = new StringBuilder("1 = 1");
            if (configs.getId() != null) {
                where.append(" and id = #{id,jdbcType=BIGINT}");
            }
            if (configs.getType() != null) {
                where.append(" and type = #{type,jdbcType=VARCHAR}");
            }
            if (configs.getKey() != null) {
                where.append(" and `key` = #{key,jdbcType=VARCHAR}");
            }
            if (configs.getValue() != null) {
                where.append(" and value = #{value,jdbcType=VARCHAR}");
            }
            if (configs.getDescribe() != null) {
                where.append(" and `describe` = #{describe,jdbcType=VARCHAR}");
            }
            if (configs.getValidFlag() != null) {
                where.append(" and valid_flag = #{validFlag,jdbcType=VARCHAR}");
            }
            if (configs.getCreateTime() != null) {
                where.append(" and create_time = #{createTime,jdbcType=VARCHAR}");
            }
            if (configs.getUpdateTime() != null) {
                where.append(" and update_time = #{updateTime,jdbcType=VARCHAR}");
            }

            return where.toString();
        }

        public String selectByPage(Configs configs) {
            return new SQL()
                    .SELECT("id, type, `key`, value, `describe`, valid_flag as validFlag, create_time as createTime, update_time as updateTime")
                    .FROM("t_configs")
                    .WHERE(getSqlWhere(configs)).toString() + " LIMIT #{length} OFFSET #{start}";
        }

        public String selectByWhere(Configs configs) {
            return new SQL()
                    .SELECT("id, type, `key`, value, `describe`, valid_flag as validFlag, create_time as createTime, update_time as updateTime")
                    .FROM("t_configs")
                    .WHERE(getSqlWhere(configs)).toString() + " LIMIT 1";
        }

        public String selectListByWhere(Configs configs) {
            return new SQL()
                    .SELECT("id, type, `key`, value, `describe`, valid_flag as validFlag, create_time as createTime, update_time as updateTime")
                    .FROM("t_configs")
                    .WHERE(getSqlWhere(configs)).toString();
        }

        public String count(Configs configs) {
            return new SQL()
                    .SELECT("count(1)")
                    .FROM("t_configs")
                    .WHERE(getSqlWhere(configs)).toString();
        }

        public String updateById(Configs configs) {
            return new SQL()
                    .UPDATE("t_configs")
                    .SET("type = #{type,jdbcType=VARCHAR}")
                    .SET("`key` = #{key,jdbcType=VARCHAR}")
                    .SET("value = #{value,jdbcType=VARCHAR}")
                    .SET("`describe` = #{describe,jdbcType=VARCHAR}")
                    .SET("valid_flag = #{validFlag,jdbcType=VARCHAR}")
                    .SET("create_time = #{createTime,jdbcType=VARCHAR}")
                    .SET("update_time = #{updateTime,jdbcType=VARCHAR}")
                    .WHERE("id = #{id,jdbcType=BIGINT}").toString();
        }

        public String updateByIdSelective(Configs configs) {
            StringBuilder strSet = new StringBuilder();
            if (configs.getType() != null) {
                strSet.append("type = #{type,jdbcType=VARCHAR}, ");
            }
            if (configs.getKey() != null) {
                strSet.append("`key` = #{key,jdbcType=VARCHAR}, ");
            }
            if (configs.getValue() != null) {
                strSet.append("value = #{value,jdbcType=VARCHAR}, ");
            }
            if (configs.getDescribe() != null) {
                strSet.append("`describe` = #{describe,jdbcType=VARCHAR}, ");
            }
            if (configs.getValidFlag() != null) {
                strSet.append("valid_flag = #{validFlag,jdbcType=VARCHAR}, ");
            }
            if (configs.getCreateTime() != null) {
                strSet.append("create_time = #{createTime,jdbcType=VARCHAR}, ");
            }
            if (configs.getUpdateTime() != null) {
                strSet.append("update_time = #{updateTime,jdbcType=VARCHAR}, ");
            }
            return "update t_configs set " + strSet.substring(0, strSet.length() - 2) + " where id = #{id,jdbcType=BIGINT}";
        }

        public String insert(Configs configs) {
            return new SQL()
                    .INSERT_INTO("t_configs")
                    .VALUES("type, `key`, value, `describe`, valid_flag, create_time, update_time",
                            "#{type,jdbcType=VARCHAR}, #{key,jdbcType=VARCHAR}, #{value,jdbcType=VARCHAR}, " +
                                    "#{describe,jdbcType=VARCHAR}, #{validFlag,jdbcType=VARCHAR}, " +
                                    "#{createTime,jdbcType=VARCHAR}, #{updateTime,jdbcType=VARCHAR}").toString();
        }

        public String insertSelective(Configs configs) {
            StringBuilder strParams = new StringBuilder();
            StringBuilder strValues = new StringBuilder();
            if (configs.getType() != null) {
                strParams.append("type, ");
                strValues.append("#{type,jdbcType=VARCHAR}, ");
            }
            if (configs.getKey() != null) {
                strParams.append("`key`, ");
                strValues.append("#{key,jdbcType=VARCHAR}, ");
            }
            if (configs.getValue() != null) {
                strParams.append("value, ");
                strValues.append("#{value,jdbcType=VARCHAR}, ");
            }
            if (configs.getDescribe() != null) {
                strParams.append("`describe`, ");
                strValues.append("#{describe,jdbcType=VARCHAR}, ");
            }
            if (configs.getValidFlag() != null) {
                strParams.append("valid_flag, ");
                strValues.append("#{validFlag,jdbcType=VARCHAR}, ");
            }
            if (configs.getCreateTime() != null) {
                strParams.append("create_time, ");
                strValues.append("#{createTime,jdbcType=VARCHAR}, ");
            }
            if (configs.getUpdateTime() != null) {
                strParams.append("update_time, ");
                strValues.append("#{updateTime,jdbcType=VARCHAR}, ");
            }
            return new SQL()
                    .INSERT_INTO("t_configs")
                    .VALUES(strParams.substring(0, strParams.length() - 2), strValues.substring(0, strValues.length() - 2)).toString();
        }

        public String batchInsert(Map<String, Object> map) {
            List<Configs> configsList = (List) map.get("list");
            StringBuilder strValues = new StringBuilder();
            for(int i = 0; i < configsList.size(); i++) {
                strValues.append(("(#{list[{i}].type,jdbcType=VARCHAR}, #{list[{i}].key,jdbcType=VARCHAR}, " +
                        "#{list[{i}].value,jdbcType=VARCHAR}, #{list[{i}].describe,jdbcType=VARCHAR}, " +
                        "#{list[{i}].validFlag,jdbcType=VARCHAR}, #{list[{i}].createTime,jdbcType=VARCHAR}, " +
                        "#{list[{i}].updateTime,jdbcType=VARCHAR}), ").replaceAll("\\{i}", i + ""));
            }
            return "insert into t_configs(type, `key`, value, `describe`, valid_flag, create_time, update_time) values "
                    + strValues.substring(0, strValues.length() - 2);
        }

    }
}