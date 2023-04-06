package com.kang.lab.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {


    public static Map<String, Object> xml2Map(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        Map<String, Object> result = getAllElements(root);
        return result;
    }

    private static Map<String, Object> getAllElements(Element element) {
        Map<String, Object> result = new HashMap();
        List<Element> childElements = element.elements();
        if (!childElements.isEmpty()) {
            if (childElements.size() == 1 || childElements.get(0).getName().equals(childElements.get(1).getName())) {
                // 当做map处理
                List<Map<String, Object>> list = new ArrayList<>();
                for (Element child : childElements) {
                    list.add(getAllElements(child));
                }
                result.put(element.getName(), list);
            } else {
                for (Element childElement : childElements) {
                    if (childElement.elements().isEmpty()) {
                        result.put(childElement.getName(), childElement.getText());
                    } else {
                        result.put(childElement.getName(), getAllElements(childElement));
                    }
                }
            }
        } else {
            result.put(element.getName(), element.getText());
        }
        return result;
    }
}
