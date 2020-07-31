package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlResponse;
import com.kang.codetool.model.CodeMakerGeneratCodeVO;
import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("codeMaker")
public class CodeMakerController {

    @ViewPage(description = "JAVA代码生成器")
    @RequestMapping("javaCode")
    public ModelAndView javaCode() {
        return new ModelAndView("tool/javaCode");
    }

    @RequestMapping("getCodeTypeSlt")
    @ResponseBody
    public KlResponse<List<String>> getCodeTypeSlt(String lang) throws ClassNotFoundException {
        KlResponse result = new KlResponse();
        Class clazz = Class.forName("com.kang.codetool.service.Generate" + lang + "CodeService");

        List<String> methodNameList = new ArrayList<>();

        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("ref")) {
                methodNameList.add(method.getName().replace("ref", ""));
            }
        }

        result.setBody(methodNameList.stream().sorted(Comparator.naturalOrder()));
        return result;
    }

    @RequestMapping("getTables")
    @ResponseBody
    public KlResponse getTables(String connectionString, String dbType) throws Exception {
        connectionString = URLDecoder.decode(connectionString.trim());

        List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connectionString, KlDatabaseType.getByName(dbType));
        if (databaseTables != null) {
            return KlResponse.success(databaseTables);
        }

        return KlResponse.fail("连接数据库失败");
    }

    @RequestMapping("generatCode")
    @ResponseBody
    public KlResponse generatCode(CodeMakerGeneratCodeVO vo) throws Exception {
        KlResponse result = new KlResponse();
        vo.setConnectionString(URLDecoder.decode(vo.getConnectionString()).trim());

        switch (vo.getDbType().toLowerCase()) {
            case "mysql":
                vo.setDatabaseType(KlDatabaseType.MySql);
                break;
            case "postgresql":
                vo.setDatabaseType(KlDatabaseType.PostgreSql);
                break;
            case "sqlserver":
                vo.setDatabaseType(KlDatabaseType.SqlServer);
                break;
            default:
                vo.setDatabaseType(KlDatabaseType.MySql);
        }

        List<KlFieldDescription> databaseColumns = Common.getDatabaseColumns(vo.getConnectionString(), vo.getTable(), KlDatabaseType.getByName(vo.getDbType()));
        vo.setFieldDescriptions(databaseColumns);

        Class clazz = Class.forName("com.kang.codetool.service.Generate" + vo.getLang() + "CodeService");

        Method method = clazz.getMethod("ref" + vo.getType(), CodeMakerGeneratCodeVO.class);

        Object model = clazz.newInstance();
        String methodResult = method.invoke(model, new Object[]{vo}).toString();

        result.setBody(methodResult);
        return result;
    }

}
