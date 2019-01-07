package com.kang.codetool.controller.tool;

import com.kang.codetool.aop.anntion.ViewPage;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlRequest;
import com.kang.codetool.common.StringCompare;
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
    public KlRequest<List<String>> getCodeTypeSlt(String lang) throws ClassNotFoundException {
        KlRequest result = new KlRequest();
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
    public KlRequest getTables(String connectionString, String dbType) {
        KlRequest result = new KlRequest();
        connectionString = URLDecoder.decode(connectionString.trim());

        List<String> databaseTables = Common.getDatabaseTables(connectionString, KlDatabaseType.getByName(dbType));

        result.setBody(databaseTables);
        return result;
    }

    @RequestMapping("generatCode")
    @ResponseBody
    public KlRequest generatCode(CodeMakerGeneratCodeVO vo) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        KlRequest result = new KlRequest();
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
