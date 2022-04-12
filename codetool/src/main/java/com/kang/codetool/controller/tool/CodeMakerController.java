package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.annotation.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.lab.utils.vo.RestResponse;
import com.kang.codetool.model.CodeMakerGeneratCodeVO;
import com.kang.lab.utils.enums.DatabaseTypeEnum;
import com.kang.lab.utils.db.FieldDescriptionUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("codeMaker")
public class CodeMakerController {

    @ViewPage(description = "JAVA代码生成器")
    @RequestMapping("javaCode")
    public ModelAndView javaCode() {
        return new ModelAndView("tool/javaCode");
    }

    @GetMapping("getCodeTypeSlt")
    @ResponseBody
    public RestResponse<List<String>> getCodeTypeSlt(String lang) throws ClassNotFoundException {
        Class clazz = Class.forName("com.kang.codetool.service.Generate" + lang + "CodeService");

        List<String> methodNameList = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("ref")) {
                methodNameList.add(method.getName().replace("ref", ""));
            }
        }
        return RestResponse.success(methodNameList.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList()));
    }

    @RequestMapping("getTables")
    @ResponseBody
    public RestResponse getTables(String connectionString, String username, String password, String dbType) throws Exception {
        connectionString = URLDecoder.decode(connectionString.trim());
        username = URLDecoder.decode(username.trim());
        password = URLDecoder.decode(password.trim());

        List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connectionString, username, password, DatabaseTypeEnum.getByName(dbType));
        if (databaseTables != null) {
            return RestResponse.success(databaseTables);
        }

        return RestResponse.error("连接数据库失败");
    }

    @RequestMapping("generatCode")
    @ResponseBody
    public RestResponse generatCode(CodeMakerGeneratCodeVO vo) throws Exception {
        vo.setConnectionString(URLDecoder.decode(vo.getConnectionString()).trim());
        vo.setUsername(URLDecoder.decode(vo.getUsername()).trim());
        vo.setPassword(URLDecoder.decode(vo.getPassword()).trim());

        switch (vo.getDbType().toLowerCase()) {
            case "mysql":
                vo.setDatabaseType(DatabaseTypeEnum.MySql);
                break;
            case "postgresql":
                vo.setDatabaseType(DatabaseTypeEnum.PostgreSql);
                break;
            case "sqlserver":
                vo.setDatabaseType(DatabaseTypeEnum.SqlServer);
                break;
            default:
                vo.setDatabaseType(DatabaseTypeEnum.MySql);
        }

        List<FieldDescriptionUtil> databaseColumns = Common.getDatabaseColumns(vo.getConnectionString(), vo.getUsername(), vo.getPassword(),
                vo.getTable(), DatabaseTypeEnum.getByName(vo.getDbType()));
        vo.setFieldDescriptions(databaseColumns);

        Class clazz = Class.forName("com.kang.codetool.service.Generate" + vo.getLang() + "CodeService");

        Method method = clazz.getMethod("ref" + vo.getType(), CodeMakerGeneratCodeVO.class);

        Object model = clazz.newInstance();
        String methodResult = method.invoke(model, new Object[]{vo}).toString();
        return RestResponse.success(methodResult);
    }

}
