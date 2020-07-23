package com.kang.codetool.controller.tool;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.kang.codetool.common.Common;
import com.kang.codetool.common.KlResponse;
import com.kang.codetool.model.CodeMakerGeneratCodeVO;
import com.kang.framework.KlString;
import com.kang.framework.KlUuid;
import com.kang.framework.KlZip;
import com.kang.framework.db.KlDatabaseType;
import com.kang.framework.db.KlFieldDescription;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("export")
public class ExportController {

    @RequestMapping("exportFile")
    public void exportFile(@Param("content") String content, @Param("fileName") String fileName, @Param("extension") String extension,
                           HttpServletResponse response) {
        try {
            content = URLDecoder.decode(content);
            // 以流的形式下载文件。
            InputStream fis = new ByteArrayInputStream(content.getBytes());
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName + "." + extension);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("exportAllFile")
    public void exportAllFile(@Param("connectionString") String connectionString, @Param("dbType") String dbType, @Param("packagePath") String packagePath,
                              @Param("lang") String lang, @Param("type") String type,
                              HttpServletResponse response) throws Exception {
        try {
            connectionString = URLDecoder.decode(connectionString);
            KlDatabaseType databaseType;
            switch (dbType.toLowerCase()) {
                case "mysql":
                    databaseType = KlDatabaseType.MySql;
                    break;
                case "postgresql":
                    databaseType = KlDatabaseType.PostgreSql;
                    break;
                case "sqlserver":
                    databaseType = KlDatabaseType.SqlServer;
                    break;
                default:
                    databaseType = KlDatabaseType.MySql;
                    break;
            }

            List<Map<String, Object>> databaseTables = Common.getDatabaseTables(connectionString, KlDatabaseType.getByName(dbType));

            String zipDirPath = this.getClass().getResource("/").getPath() + "zip/";

            String zipFileName = KlUuid.newUuid();
            String fullPathName = zipDirPath + zipFileName + "/";

            File zipDir = new File(zipDirPath);
            File zipDirFullPath = new File(fullPathName);
            if (databaseTables.size() > 0) {
                if (!zipDir.exists()) {
                    zipDir.mkdir();
                }

                zipDirFullPath.mkdir();
            }
            for (Map<String, Object> table : databaseTables) {
                String tableName = table.get("TABLE_NAME").toString();
                List<KlFieldDescription> databaseColumns = Common.getDatabaseColumns(connectionString, tableName, KlDatabaseType.getByName(dbType));
                String className = KlString.toUpperFirst(KlString.replaceUnderline(tableName.startsWith("t_") ? tableName.replace("t_", "") : tableName));
                CodeMakerGeneratCodeVO outModel = new CodeMakerGeneratCodeVO();
                outModel.setPackagePath(packagePath);
                outModel.setFieldDescriptions(databaseColumns);
                outModel.setDatabaseType(databaseType);
                outModel.setClassName(className);
                outModel.setTable(tableName);
                Class clazz = Class.forName("com.kang.codetool.service.Generate" + lang + "CodeService");
                //声明创建当前类实例
                Method method = clazz.getMethod("ref" + type, CodeMakerGeneratCodeVO.class);

                String result = method.invoke(clazz.newInstance(), new Object[]{outModel}).toString();

                //下载文件到文件夹内
                String fileName = "";

                if (lang.toLowerCase() == "dotnet") {
                    if (type.toLowerCase().startsWith("dao")) {
                        fileName = className + "Dao.cs";
                    } else {
                        fileName = className + ".cs";
                    }
                } else {
                    if (type.toLowerCase().endsWith("xml")) {
                        fileName = className + "Mapper.xml";
                    } else if (type.toLowerCase().endsWith("mapper")) {
                        fileName = className + "Mapper.java";
                    } else if (type.toLowerCase().startsWith("dao")) {
                        fileName = className + "Dao.java";
                    } else {
                        fileName = className + ".java";
                    }
                }

                String fullName = zipDirFullPath + "/" + fileName;
                File f = new File(fullName);
                if (!f.exists()) {
                    f.createNewFile();
                    FileWriter fw = new FileWriter(f.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(result);
                    bw.close();
                }
            }

            if (databaseTables.size() > 0) {
                KlZip.compress(fullPathName, zipDir + "\\" + zipFileName + ".zip");
                zipDirFullPath.delete();
            }
            InputStream fis = new BufferedInputStream(new FileInputStream(zipDir + "/" + zipFileName + ".zip"));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + zipFileName + ".zip");
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("exportExcel")
    public void exportExcel(@Param("content") String content, @Param("fileName") String fileName,
                            HttpServletResponse response) throws Exception {
        KlResponse result = new KlResponse();
        List<ExcelInfo> list = new ArrayList<ExcelInfo>();
        ExcelInfo info = new ExcelInfo();

        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, BaseRowModel.class);
        sheet.setSheetName("导出数据");
        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        //创建本地文件
        String filePath = fileName + ".xlsx";
        File dbfFile = new File(filePath);
        try {
            if (!dbfFile.exists() || dbfFile.isDirectory()) {
                dbfFile.createNewFile();
            }
            fileName = new String(filePath.getBytes(), "ISO-8859-1");
            response.addHeader("Content-Disposition", "filename=" + fileName);
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("创建文件失败！");
        }
    }

    class ExcelInfo extends BaseRowModel {

    }
}
