package com.kang.codetool.service;

import com.kang.codetool.model.CodeMakerGeneratCodeVO;
import com.kang.framework.KlMybatisTypeMap;
import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;
import com.kang.framework.KlDbTypeMap;
import com.kang.framework.KlString;

public class GenerateJavaCodeService {

    public String refEntity(CodeMakerGeneratCodeVO vo) {
        StringBuilder result = new StringBuilder();
        StringBuilder getterAndSetter = new StringBuilder();

        result.append(KlString.format("package {0};\n\n" +
                "import java.util.*;\n" +
                "import java.math.*;\n\n" +
                "public class {1} {\n\n", vo.getPackagePath().replace("{0}", "model"), KlString.toUpperFirst(KlString.replaceUnderline(vo.getClassNameResult()))));

        for (KlFieldDescription f : vo.getFieldDescriptions()) {
            if (!KlString.isBlank(f.getDescription()))
                result.append("    /**\n" +
                        "    * " + f.getDescription().replace("\n", "  ") + "\n" +
                        "    */\n");
            result.append("    private " + KlDbTypeMap.map4J(f.getDbType(), true, vo.getDatabaseType()) + " " + KlString.toLowerFirst(f.getSimpleName()) + ";\n\n");

            getterAndSetter.append(KlString.format(
                    "    public {2} get{1}() {\n" +
                            "        return {0};\n" +
                            "    }\n" +
                            "    public void set{1}({2} {0}) {\n" +
                            "        this.{0} = {0};\n" +
                            "    }\n", KlString.toLowerFirst(f.getSimpleName()), KlString.toUpperFirst(f.getSimpleName()),
                    KlDbTypeMap.map4J(f.getDbType(), true, vo.getDatabaseType())));

        }
        result.append(getterAndSetter);
        result.append("\n}");

        return result.toString();
    }

    public String refCloneObject(CodeMakerGeneratCodeVO vo) {
        StringBuilder result = new StringBuilder();

        for (KlFieldDescription f : vo.getFieldDescriptions()) {
            result.append(KlString.format("model.set{0}(vo.get{0}());\n", KlString.toUpperFirst(f.getSimpleName())));
        }

        return result.toString();
    }

    /**
     * Dao通用代码
     */
    private static String daoBaseCode(CodeMakerGeneratCodeVO vo, String content) {
        StringBuilder result = new StringBuilder();

        result.append(KlString.format("package {0};\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.util.*;\n" +
                "import java.math.*;\n" +
                "\n" +
                "import javax.sql.rowset.CachedRowSet;\n" +
                "\n" +
                "import {2}.{1};\n" +
                "import com.kang.framework.*;\n" +
                "\n" +
                "        public class {1}Dao{\n" +
                "        {3}\n" +
                "\n" +
                "\n" +
                "    }", KlString.format(vo.getPackagePath(), "dao"), KlString.toUpperFirst(KlString.replaceUnderline(vo.getClassNameResult())), KlString.format(vo.getPackagePath(), "model"), content));

        return result.toString();
    }

    public String refMybatisMapper(CodeMakerGeneratCodeVO vo) {
        StringBuilder field_Basic = new StringBuilder();
        StringBuilder field_Add = new StringBuilder();
        StringBuilder fieldValue = new StringBuilder();
        StringBuilder fieldParams = new StringBuilder();
        StringBuilder field_SqlContent = new StringBuilder();
        KlFieldDescription field = new KlFieldDescription();
        if (vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
            field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElseGet(null);
        } else {
            field = vo.getFieldDescriptions().stream().findFirst().orElseGet(null);
        }
        StringBuilder codeStr = new StringBuilder();
        codeStr.append(KlString.format(
                "package {0};\n" +
                        "\n" +
                        "import {1};\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public interface I{2}Dao{\n" +
                        "\n" +
                        "        {7}By{3}({4} {5});\n" +
                        "\n" +
                        "        List<{2}> selectAll();\n" +
                        "\n" +
                        "        List<{2}> selectByPage({2} {6});\n" +
                        "\n" +
                        "        {2} selectByWhere({2} {6});\n" +
                        "\n" +
                        "        List<{2}> selectListByWhere({2} {6});\n" +
                        "\n" +
                        "        int count({2} {6});\n" +
                        "\n" +
                        "        int deleteBy{3}({4} {5});\n" +
                        "\n" +
                        "        int updateBy{3}({2} {6});\n" +
                        "\n" +
                        "        int updateBy{3}Selective({2} {6});\n" +
                        "\n" +
                        "        int insert({2} {6});\n" +
                        "\n" +
                        "        int insertSelective({2} {6});\n" +
                        "\n" +
                        "        void batchInsert(List<{2}> {6}List);\n" +
                        "    \n" +
                        "}",
                KlString.format(vo.getPackagePath(), "dao"),
                KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName(),
                vo.getClassName(),
                KlString.toUpperFirst(field.getSimpleName()),
                KlDbTypeMap.map4J(field.getDbType(), true, vo.getDatabaseType()),
                KlString.toLowerFirst(field.getSimpleName()),
                KlString.toLowerFirst(vo.getClassName()),
                vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey())) ? vo.getClassName() + " select" : "List<" + vo.getClassName() + "> selectList"))
        ;

        return codeStr.toString();
    }

    public String refMybatisMapperWithAnnotation(CodeMakerGeneratCodeVO vo) {
        StringBuilder field_Basic = new StringBuilder();
        StringBuilder fieldInsert = new StringBuilder();
        StringBuilder fieldValue = new StringBuilder();
        StringBuilder fieldParams = new StringBuilder();
        StringBuilder field_SqlContent = new StringBuilder();
        StringBuilder field_SqlWhere = new StringBuilder();
        KlFieldDescription field;
        if (vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
            field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElseGet(null);
        } else {
            field = vo.getFieldDescriptions().stream().findFirst().orElseGet(null);
        }
        vo.getFieldDescriptions().forEach(f -> {
            fieldInsert.append("");

            appendLineBreak(fieldParams, 100);
            fieldParams.append(f.getName() + ", ");

            field_SqlWhere.append(KlString.format(
                    "                if ({1}.get{0}() != null) {\n" +
                            "                    where.append(\" and {2} = #{{3},jdbcType={4}}\");\n" +
                            "                }\n",
                    KlString.toUpperFirst(f.getSimpleName()),
                    KlString.toLowerFirst(vo.getClassName()),
                    f.getName(),
                    KlString.toLowerFirst(f.getSimpleName()),
                    KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType())
            ));
        });
        StringBuilder codeStr = new StringBuilder();
        codeStr.append(KlString.format(
                "package {0};\n" +
                        "\n" +
                        "import {1};\n" +
                        "import org.apache.ibatis.annotations.*;\n" +
                        "import org.apache.ibatis.jdbc.SQL;\n" +
                        "import java.util.List;\n" +
                        "\n" +
                        "public interface I{2}Dao{\n" +
                        "\n" +
                        "        @Select(\"select * from {8} where {9} = #{{5}}\")\n" +
                        "        {7}By{3}({4} {5});\n" +
                        "\n" +
                        "        @Select(\"select * from {8}\")\n" +
                        "        List<{2}> selectAll();\n" +
                        "\n" +
                        "        @SelectProvider(type = {2}Provider.class, method = \"selectByPage\")\n" +
                        "        List<{2}> selectByPage({2} {6});\n" +
                        "\n" +
                        "        @SelectProvider(type = {2}Provider.class, method = \"selectByWhere\")\n" +
                        "        {2} selectByWhere({2} {6});\n" +
                        "\n" +
                        "        @SelectProvider(type = {2}Provider.class, method = \"selectListByWhere\")\n" +
                        "        List<{2}> selectListByWhere({2} {6});\n" +
                        "\n" +
                        "        @SelectProvider(type = {2}Provider.class, method = \"count\")\n" +
                        "        int count({2} {6});\n" +
                        "\n" +
                        "        @Delete(\"delete {8} where {9}=#{{5}}\")\n" +
                        "        int deleteBy{3}({4} {5});\n" +
                        "\n" +
                        "        @UpdateProvider(type = {2}Provider.class, method = \"updateBy{3}\")\n" +
                        "        int updateBy{3}({2} {6});\n" +
                        "\n" +
                        "        @UpdateProvider(type = {2}Provider.class, method = \"updateBy{3}Selective\")\n" +
                        "        int updateBy{3}Selective({2} {6});\n" +
                        "\n" +
                        "        @InsertProvider(type = {2}Provider.class, method = \"insert\")\n" +
                        "        @Options(useGeneratedKeys = true, keyProperty = \"{9}\")\n" +
                        "        int insert({2} {6});\n" +
                        "\n" +
                        "        @InsertProvider(type = {2}Provider.class, method = \"insertSelective\")\n" +
                        "        @Options(useGeneratedKeys = true, keyProperty = \"{9}\")\n" +
                        "        int insertSelective({2} {6});\n" +
                        "\n" +
                        "        @InsertProvider(type = {2}Provider.class, method = \"batchInsert\")\n" +
                        "        void batchInsert(@Param(\"list\") List<{2}> {6}List);\n" +
                        "\n" +
                        "        class {2}Provider extends SQL {\n" +
                        "\n" +
                        "            private String getSqlWhere({2} {6}) {\n" +
                        "                StringBuilder where = new StringBuilder(\"1 = 1\");\n" +
                        "{10}\n" +
                        "                return where.toString();\n" +
                        "            }\n\n" +
                        generateSelectMethodCode(vo) +
                        generateUpdateMethodCode(vo) +
                        generateInsertMethodCode(vo) +
                        "        }\n" +
                        "}",
                KlString.format(vo.getPackagePath(), "dao"),
                KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName(),
                vo.getClassName(),
                KlString.toUpperFirst(field.getSimpleName()),
                KlDbTypeMap.map4J(field.getDbType(), true, vo.getDatabaseType()),
                KlString.toLowerFirst(field.getSimpleName()),
                KlString.toLowerFirst(vo.getClassName()),
                vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey())) ? vo.getClassName() + " select" : "List<" + vo.getClassName() + "> selectList",
                vo.getTable(),
                field.getName(),
                field_SqlWhere,
                fieldParams))
        ;

        return codeStr.toString();
    }

    public String generateSelectMethodCode(CodeMakerGeneratCodeVO vo) {
        StringBuilder fieldParams = new StringBuilder();
        vo.getFieldDescriptions().forEach(f -> {
            appendLineBreak(fieldParams, 100);
            fieldParams.append(f.getName() + ", ");
        });
        return KlString.format(
                "            public String selectByPage({1} {2}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    SELECT(\"{0}\");\n" +
                        "                    FROM(\"{3}\");\n" +
                        "                    WHERE(getSqlWhere({2}));\n" +
                        "                }}.toString() + \" LIMIT #{length} OFFSET #{start}\";\n" +
                        "            }\n\n" +
                        "            public String selectByWhere({1} {2}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    SELECT(\"{0}\");\n" +
                        "                    FROM(\"{3}\");\n" +
                        "                    WHERE(getSqlWhere({2}));\n" +
                        "                }}.toString() + \" LIMIT 1\";\n" +
                        "            }\n\n" +
                        "            public String selectListByWhere({1} {2}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    SELECT(\"{0}\");\n" +
                        "                    FROM(\"{3}\");\n" +
                        "                    WHERE(getSqlWhere({2}));\n" +
                        "                }}.toString();\n" +
                        "            }\n\n" +
                        "            public String count({1} {2}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    SELECT(\"count(1)\");\n" +
                        "                    FROM(\"{3}\");\n" +
                        "                    WHERE(getSqlWhere({2}));\n" +
                        "                }}.toString();\n" +
                        "            }\n\n",
                fieldParams.substring(0, fieldParams.length() - 2),
                vo.getClassName(),
                KlString.toLowerFirst(vo.getClassName()),
                vo.getTable());
    }

    public String generateUpdateMethodCode(CodeMakerGeneratCodeVO vo) {
        KlFieldDescription field;
        if (vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
            field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElseGet(null);
        } else {
            field = vo.getFieldDescriptions().stream().findFirst().orElseGet(null);
        }
        StringBuilder fieldParams = new StringBuilder();
        StringBuilder fieldUpdateSet = new StringBuilder();
        StringBuilder fieldUpdateSetSelective = new StringBuilder();
        vo.getFieldDescriptions().forEach(f -> {
            appendLineBreak(fieldParams, 100);
            fieldParams.append(f.getName() + ", ");

            appendLineBreak(fieldUpdateSet, 100);
            fieldUpdateSet.append("                    SET(\"" + f.getName() + " = #{" + f.getSimpleName() + ",jdbcType=" + KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()) + "}\");\n");

            appendLineBreak(fieldUpdateSetSelective, 100);
            fieldUpdateSetSelective.append(KlString.format(
                    "                if ({1}.get{0}() != null) {\n" +
                            "                    strSet.append(\"{2} = #{{3},jdbcType={4}}, \");\n" +
                            "                }\n",
                    KlString.toUpperFirst(f.getSimpleName()),
                    KlString.toLowerFirst(vo.getClassName()),
                    f.getName(),
                    KlString.toLowerFirst(f.getSimpleName()),
                    KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType())));

        });
        return KlString.format(
                "            public String updateBy{0}({1} {2}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    UPDATE(\"{3}\");\n" +
                        "{6}" +
                        "                    WHERE(\"{4} = #{{5},jdbcType={7}}\");\n" +
                        "                }}.toString();\n" +
                        "            }\n\n" +
                        "            public String updateBy{0}Selective({1} {2}) {\n" +
                        "                StringBuilder strSet = new StringBuilder();\n" +
                        "{8}" +
                        "                return \"update {3} set \" + strSet +\" where {4} = #{{5},jdbcType={7}}\";\n" +
                        "            }\n\n",
                KlString.toUpperFirst(field.getSimpleName()),
                vo.getClassName(),
                KlString.toLowerFirst(vo.getClassName()),
                vo.getTable(),
                field.getName(),
                KlString.toLowerFirst(field.getSimpleName()),
                fieldUpdateSet,
                KlMybatisTypeMap.map4MybatisPostgreSql(field.getDbType()),
                fieldUpdateSetSelective);
    }

    public String generateInsertMethodCode(CodeMakerGeneratCodeVO vo) {
        KlFieldDescription field;
        if (vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
            field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElseGet(null);
        } else {
            field = vo.getFieldDescriptions().stream().findFirst().orElseGet(null);
        }
        StringBuilder fieldInsertParams = new StringBuilder();
        StringBuilder fieldInsertValues = new StringBuilder();
        StringBuilder fieldInsertValuesBatch = new StringBuilder();
        StringBuilder fieldInsertSelective = new StringBuilder();
        vo.getFieldDescriptions().forEach(f -> {
            if(!f.getIsIdentity()){
                appendLineBreak(fieldInsertParams, 100);
                fieldInsertParams.append(f.getName() + ", ");

                appendLineBreak(fieldInsertValues, 50, "                       \"", "\" +");
                fieldInsertValues.append("#{" + KlString.toLowerFirst(f.getSimpleName()) + ",jdbcType=" + KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()) + "}, ");

                appendLineBreak(fieldInsertValuesBatch, 50, "                       \"", "\" +");
                fieldInsertValuesBatch.append("#{list[{i}]." + KlString.toLowerFirst(f.getSimpleName()) + ",jdbcType=" + KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()) + "}, ");
            }

            appendLineBreak(fieldInsertSelective, 100);
            fieldInsertSelective.append(KlString.format(
                    "                if ({1}.get{0}() != null) {\n" +
                            "                    strParams.append(\"{2}, \");\n" +
                            "                    strValues.append(\"#{{3},jdbcType={4}}, \");\n" +
                            "                }\n",
                    KlString.toUpperFirst(f.getSimpleName()),
                    KlString.toLowerFirst(vo.getClassName()),
                    f.getName(),
                    KlString.toLowerFirst(f.getSimpleName()),
                    KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType())));
        });

        return KlString.format(
                "            public String insert({0} {1}) {\n" +
                        "               return new SQL() {{\n" +
                        "                    INSERT_INTO(\"{5}\");\n" +
                        "                    VALUES(\"{6}\", \n" +
                        "                       \"{7}\");\n" +
                        "                }}.toString();\n" +
                        "            }\n\n" +
                        "            public String insertSelective({0} {1}) {\n" +
                        "               StringBuilder strParams = new StringBuilder();\n" +
                        "               StringBuilder strValues = new StringBuilder();\n" +
                        "{8}" +
                        "               return new SQL() {{\n" +
                        "                    INSERT_INTO(\"{5}\");\n" +
                        "                    VALUES(strParams.substring(0, strParams.length() - 2), strValues.substring(0, strValues.length() - 2));\n" +
                        "                }}.toString();\n" +
                        "            }\n\n" +
                        "            public String batchInsert(Map<String, Object> map) {\n" +
                        "               List<{0}> {1}List = (List) map.get(\"list\");\n" +
                        "               StringBuilder strValues = new StringBuilder();\n" +
                        "               for(int i = 0; i < {1}List.size(); i++) {\n" +
                        "                   strValues.append(\"({9}), \".replaceAll(\"{i}\", i));\n" +
                        "               }\n" +
                        "               return \"insert into {5}({6}) values \" + strValues.substring(0, strValues.length() - 2);\n" +
                        "            }\n\n",
                vo.getClassName(),
                KlString.toLowerFirst(vo.getClassName()),
                KlString.toUpperFirst(field.getSimpleName()),
                KlString.toLowerFirst(field.getSimpleName()),
                field.getName(),
                vo.getTable(),
                fieldInsertParams.substring(0, fieldInsertParams.length() - 2),
                fieldInsertValues.substring(0, fieldInsertValues.length() - 2),
                fieldInsertSelective,
                fieldInsertValuesBatch.substring(0, fieldInsertValues.length() - 2));
    }

    public String refMybatisMapperXml(CodeMakerGeneratCodeVO vo) {
        StringBuilder field_Basic = new StringBuilder();
        StringBuilder field_Add = new StringBuilder();
        StringBuilder fieldValue = new StringBuilder();
        StringBuilder fieldParams = new StringBuilder();
        StringBuilder fieldWhereParams = new StringBuilder();
        StringBuilder field_SqlContent = new StringBuilder();

        StringBuilder codeStr = new StringBuilder();
        codeStr.append("<!DOCTYPE mapper PUBLIC \" -//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "<mapper namespace=\"" + KlString.format(vo.getPackagePath(), "dao") + ".I" + KlString.toUpperFirst(vo.getClassName()) + "Dao\">\n");

        vo.getFieldDescriptions().forEach(f ->
        {
            field_Basic.append(KlString.format("    <{0} column=\"" + f.getName() + "\" property=\"" + KlString.toLowerFirst(f.getSimpleName()) + "\" jdbcType=\""
                            + KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase() + "\" />\n",
                    "PRI".equals(f.getColumnKey()) ? "id" : "result"));

            appendLineBreak(fieldParams, 100, "    ");
            fieldParams.append(f.getName() + ", ");

            fieldWhereParams.append(KlString.format("\n    <if test=\"{0} != null\">\n" +
                    "        AND {1} = #{{0},jdbcType={2}}\n" +
                    "    </if>", f.getSimpleName(), f.getName(), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase()));
        });

        field_SqlContent.append(GenerateCode.mybatisSelect(vo));
        field_SqlContent.append(GenerateCode.mybatisDelete(vo));
        field_SqlContent.append(GenerateCode.mybatisInsert(vo));
        field_SqlContent.append(GenerateCode.mybatisUpdate(vo));

        codeStr.append(KlString.format(
                "  <resultMap id=\"BaseResultMap\" type=\"{0}\">\n" +
                        "{1}  </resultMap>\n" +
                        "  <sql id=\"Base_Column_List\">\n" +
                        "    {2}\n" +
                        "  </sql>\n" +
                        "  <sql id=\"Where_Column_List\">{4}\n" +
                        "  </sql>\n" +
                        "{3}\n</mapper>",
                KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName(),
                field_Basic,
                fieldParams.toString().substring(0, fieldParams.length() - 2),
                field_SqlContent,
                fieldWhereParams.toString()));

        return codeStr.toString();
    }

    public static StringBuilder appendLineBreak(StringBuilder content, int maxLength) {
        return appendLineBreak(content, maxLength, "");
    }

    private static StringBuilder appendLineBreak(StringBuilder content, int maxLength, String newLineHead) {
        String[] strArr = content.toString().split("\n");
        if (strArr[strArr.length - 1].length() > maxLength) {
            return content.append("\n" + newLineHead);
        }
        return content;
    }

    private static StringBuilder appendLineBreak(StringBuilder content, int maxLength, String newLineStart, String oldLineEnd) {
        String[] strArr = content.toString().split("\n");
        if (strArr[strArr.length - 1].length() > maxLength) {
            return content.append(oldLineEnd + "\n" + newLineStart);
        }
        return content;
    }

    static class GenerateCode {


        public static String mybatisSelect(CodeMakerGeneratCodeVO vo) {

            StringBuilder result = new StringBuilder();

            if (vo.getFieldDescriptions().size() > 0) {
                KlFieldDescription field = new KlFieldDescription();
                if (vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey()))) {
                    field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElse(null);
                } else {
                    field = vo.getFieldDescriptions().get(0);
                }

                result.append(KlString.format(
                        "  <select id=\"{6}By{1}\" parameterType=\"{4}\" resultMap=\"BaseResultMap\">\n" +
                                "    select\n" +
                                "    <include refid=\"Base_Column_List\" />\n" +
                                "    from {0}\n" +
                                "    where {5} = #{{2},jdbcType={3}}\n" +
                                "  </select>\n" +
                                "  <select id=\"selectAll\" resultMap=\"BaseResultMap\">\n" +
                                "    select\n" +
                                "    <include refid=\"Base_Column_List\" />\n" +
                                "    from {0}\n" +
                                "  </select>\n" +
                                "  <select id=\"selectByPage\" parameterType=\"{7}\" resultMap=\"BaseResultMap\">\n" +
                                "    select\n" +
                                "    <include refid=\"Base_Column_List\" />\n" +
                                "    from {0}\n" +
                                "    <trim prefix=\"where\" prefixOverrides=\"and | or\">\n" +
                                "        <include refid = \"Where_Column_List\"></include>\n" +
                                "    </trim>\n" +
                                "    LIMIT #{length} OFFSET #{start}\n" +
                                "  </select>\n" +
                                "  <select id=\"selectByWhere\" parameterType=\"{7}\" resultMap=\"BaseResultMap\">\n" +
                                "    select\n" +
                                "    <include refid=\"Base_Column_List\" />\n" +
                                "    from {0}\n" +
                                "    <trim prefix=\"where\" prefixOverrides=\"and | or\">\n" +
                                "        <include refid = \"Where_Column_List\"></include>\n" +
                                "    </trim>\n" +
                                "    LIMIT 1\n" +
                                "  </select>\n" +
                                "  <select id=\"selectListByWhere\" parameterType=\"{7}\" resultMap=\"BaseResultMap\">\n" +
                                "    select\n" +
                                "    <include refid=\"Base_Column_List\" />\n" +
                                "    from {0}\n" +
                                "    <trim prefix=\"where\" prefixOverrides=\"and | or\">\n" +
                                "        <include refid = \"Where_Column_List\"></include>\n" +
                                "    </trim>\n" +
                                "  </select>\n" +
                                "  <select id=\"count\" parameterType=\"{7}\" resultType=\"Integer\">\n" +
                                "    select count(1)\n" +
                                "    from {0}\n" +
                                "    <trim prefix=\"where\" prefixOverrides=\"and | or\">\n" +
                                "        <include refid = \"Where_Column_List\"></include>\n" +
                                "    </trim>\n" +
                                "  </select>\n", vo.getTable(), KlString.toUpperFirst(field.getName()),
                        KlString.toLowerFirst(field.getSimpleName()), KlMybatisTypeMap.map4MybatisPostgreSql(field.getDbType()).toUpperCase(),
                        KlDbTypeMap.map4J(field.getDbType(), true, vo.getDatabaseType()), field.getName(),
                        vo.getFieldDescriptions().stream().anyMatch(f -> "PRI".equals(f.getColumnKey())) ? "select" : "selectList",
                        KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName()));
            }
            return result.toString();
        }

        public static String mybatisUpdate(CodeMakerGeneratCodeVO vo) {

            StringBuilder result = new StringBuilder();
            StringBuilder field_UpdateParams = new StringBuilder();
            StringBuilder field_UpdateParamsSelective = new StringBuilder();

            if (vo.getFieldDescriptions().size() == 0) {
                return "";
            }
            vo.getFieldDescriptions().stream().filter(f -> f.getColumnKey() != "PRI").forEach(f ->
            {
                field_UpdateParams.append(KlString.format("{2} = #{{0},jdbcType={1}}, ", KlString.toLowerFirst(f.getSimpleName()), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase(), f.getName()));
                appendLineBreak(field_UpdateParams, 100, "    ");

                field_UpdateParamsSelective.append(KlString.format(
                        "      <if test=\"{0} != null\">\n" +
                                "        {1} = #{{0},jdbcType={2}},\n" +
                                "      </if>\n", KlString.toLowerFirst(f.getSimpleName()), f.getName(), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase()));
            });

            KlFieldDescription field = new KlFieldDescription();
            if (vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).count() == 1) {
                field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElse(null);
            } else {
                field = vo.getFieldDescriptions().stream().findFirst().orElse(null);
            }

            result.append(KlString.format(
                    "  <update id=\"updateBy{7}\" parameterType=\"{4}\">\n" +
                            "    update {0}\n" +
                            "    set {6}\n" +
                            "    where {1} = #{{2},jdbcType={3}}\n" +
                            "  </update>\n" +
                            "  <update id=\"updateBy{7}Selective\" parameterType=\"{4}\">\n" +
                            "    update {0}\n" +
                            "    <set>\n" +
                            "{5}    </set>\n" +
                            "    where {1} = #{{2},jdbcType={3}}\n" +
                            "  </update>", vo.getTable(), field.getName(), KlString.toLowerFirst(field.getSimpleName()),
                    KlMybatisTypeMap.map4MybatisPostgreSql(field.getDbType()).toUpperCase(), KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName(),
                    field_UpdateParamsSelective, field_UpdateParams.toString().trim().substring(0, field_UpdateParams.toString().trim().length() - 1), KlString.toUpperFirst(field.getSimpleName())))
            ;
            return result.toString();
        }

        public static String mybatisInsert(CodeMakerGeneratCodeVO vo) {

            StringBuilder result = new StringBuilder();
            StringBuilder field_Params = new StringBuilder();
            StringBuilder field_InsertParams = new StringBuilder();
            StringBuilder field_InsertParams_Batch = new StringBuilder();
            StringBuilder field_InsertSelectiveParams = new StringBuilder();
            StringBuilder field_InsertSelectiveValues = new StringBuilder();

            if (vo.getFieldDescriptions().size() == 0) {
                return "";
            }
            vo.getFieldDescriptions().stream().filter(f -> !f.getIsIdentity()).forEach(f ->
            {
                appendLineBreak(field_Params, 100, "    ");
                field_Params.append(f.getName() + ", ");

                appendLineBreak(field_InsertParams, 100, "    ");
                field_InsertParams.append(KlString.format("    #{{0},jdbcType={1}},\n", KlString.toLowerFirst(f.getSimpleName()), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase()));

                appendLineBreak(field_InsertParams_Batch, 100, "        ");
                field_InsertParams_Batch.append(KlString.format("#{item.{0},jdbcType={1}}, ", KlString.toLowerFirst(f.getSimpleName()), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase()));

                field_InsertSelectiveParams.append(KlString.format(
                        "      <if test=\"{0} != null\">\n" +
                                "        {1},\n" +
                                "      </if>\n", KlString.toLowerFirst(f.getSimpleName()), f.getName()));

                field_InsertSelectiveValues.append(KlString.format(
                        "      <if test=\"{0} != null\">\n" +
                                "        #{{0},jdbcType={1}},\n" +
                                "      </if>\n", KlString.toLowerFirst(f.getSimpleName()), KlMybatisTypeMap.map4MybatisPostgreSql(f.getDbType()).toUpperCase()));
            });

            result.append(KlString.format(
                    "  <insert id=\"insert\" parameterType=\"{3}\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n" +
                            "    insert into {0}(\n" +
                            "    {1}\n" +
                            "    ) values (\n" +
                            "    {2})\n" +
                            "  </insert>\n",
                    vo.getTable(),
                    field_Params.toString().substring(0, field_Params.length() - 2),
                    field_InsertParams.toString().trim().substring(0, field_InsertParams.toString().trim().length() - 1),
                    KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName()));

            result.append(KlString.format(
                    "  <insert id=\"insertSelective\" parameterType=\"{3}\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n" +
                            "    insert into {0}\n" +
                            "    <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">\n" +
                            "{1}    </trim>\n" +
                            "    <trim prefix=\"values(\" suffix=\")\" suffixOverrides=\",\">\n" +
                            "{2}    </trim>\n" +
                            "  </insert>\n",
                    vo.getTable(),
                    field_InsertSelectiveParams.toString(),
                    field_InsertSelectiveValues.toString(),
                    KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName()));

            result.append(KlString.format(
                    "  <insert id=\"batchInsert\" parameterType=\"java.util.List\">\n" +
                            "    insert into {0}(\n" +
                            "    {1}\n" +
                            "    ) values \n" +
                            "      <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">\n" +
                            "        ({2})\n" +
                            "      </foreach>\n" +
                            "  </insert>",
                    vo.getTable(),
                    field_Params.toString().substring(0, field_Params.length() - 2),
                    field_InsertParams_Batch.toString().trim().substring(0, field_InsertParams_Batch.toString().trim().length() - 1).trim(),
                    KlString.format(vo.getPackagePath(), "model") + "." + vo.getClassName()));
            return result.toString();
        }

        public static String mybatisDelete(CodeMakerGeneratCodeVO vo) {

            StringBuilder result = new StringBuilder();

            if (vo.getFieldDescriptions().size() == 0) {
                return "";
            }
            KlFieldDescription field = new KlFieldDescription();
            if (vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).count() == 1) {
                field = vo.getFieldDescriptions().stream().filter(f -> "PRI".equals(f.getColumnKey())).findFirst().orElse(null);
            } else {
                field = vo.getFieldDescriptions().stream().findFirst().orElse(null);
            }

            result.append(KlString.format(
                    "  <delete id=\"deleteBy{1}\" parameterType=\"{4}\">\n" +
                            "    delete from {0}\n" +
                            "    where {5} = #{{2},jdbcType={3}}\n" +
                            "  </delete>\n", vo.getTable(), KlString.toUpperFirst(field.getName()), KlString.toLowerFirst(field.getSimpleName()),
                    KlMybatisTypeMap.map4MybatisPostgreSql(field.getDbType()).toUpperCase(), KlDbTypeMap.map4J(field.getDbType(), true, vo.getDatabaseType()), field.getName()));

            return result.toString();
        }
    }
}
