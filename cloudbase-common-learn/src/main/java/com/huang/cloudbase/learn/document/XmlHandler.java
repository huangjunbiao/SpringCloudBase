package com.huang.cloudbase.learn.document;

import cn.hutool.core.date.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 针对xml的读写，此处以rss内容源为例，主要按级构建填充element节点
 *
 * @author huangjunbiao_cdv
 */
public class XmlHandler {
    private static final Logger logger = LoggerFactory.getLogger(XmlHandler.class);

    /**
     * 创建xml文件，
     *
     * @param name 文件路径名称
     * @param map  填充内容值
     * @return 是否成功创建
     */
    private static boolean createXml(String name, Map<String, Object> map) {
        File file = new File(name);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                boolean b = parentFile.mkdirs();
                if (!b) return false;
                Document document = DocumentHelper.createDocument();
                generateDocumentElement(document, map);
                OutputFormat format = OutputFormat.createPrettyPrint();
                // 设置编码格式
                format.setEncoding("UTF-8");
                try {
                    XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
                    // 设置是否转义，默认使用转义字符
                    writer.setEscapeText(false);
                    writer.write(document);
                    writer.close();
                    logger.info("创建xml文件成功");
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        //已存在
        return true;
    }

    /**
     * 追加item节点到文件
     *
     * @param itemMap 子节点内容数据
     * @return 是否成功
     */
    private static boolean appendDocumentItem(String name, Map<String, Object> itemMap) {
        SAXReader reader = new SAXReader();
        File file = new File(name);
        if (!file.exists()) {
            return false;
        }
        try {
            Document document = reader.read(file);
            Element rss = document.getRootElement();
            Element channel = rss.element("channel");
            Element item = channel.addElement("item");
            generateDocumentItem(item, itemMap);
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            logger.info("追加子节点item到xml成功");
            return true;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean changeDocumentItem(String name, Map<String, Object> itemMap) {
        SAXReader reader = new SAXReader();
        File file = new File(name);
        if (!file.exists()) {
            //路径下xml文件不存在直接返回，认为错误
            logger.error("xml文件不存在！！！");
            return false;
        } else {
            try {
                Document document = reader.read(file);
                Element rss = document.getRootElement();
                Element channel = rss.element("channel");
                List<Element> elements = channel.elements("item");

                for (Element element : elements) {
                    if (element.element("link").getText().equals(itemMap.get("link"))) {
                        element.element("title").setText((String) itemMap.get("title"));
                        element.element("description").clearContent();
                        element.element("description").addCDATA((String) itemMap.get("description"));
                        element.element("pubDate").setText(DateUtil.formatDateTime((Date) itemMap.get("pubDate")));
                        break;
                    }
                }
                OutputFormat format = OutputFormat.createPrettyPrint();
                // 设置编码格式
                format.setEncoding("UTF-8");
                XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
                // 设置是否转义，默认使用转义字符
                writer.setEscapeText(false);
                writer.write(document);
                writer.close();
                return true;
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 构建xml的主要节点
     *
     * @param document xml文档
     * @param map      填充内容值
     */
    @SuppressWarnings("unchecked")
    private static void generateDocumentElement(Document document, Map<String, Object> map) {
        //添加rss节点
        Element rss = document.addElement("rss");
        rss.addAttribute("version", "2.0");
        // 添加channel节点
        Element channel = rss.addElement("channel");
        Element title = channel.addElement("title");
        title.setText((String) map.get("title"));
        Element description = channel.addElement("description");
        description.setText(map.get("description") + "频道");
        Element link = channel.addElement("link");
        link.setText((String) map.get("link"));
        Element language = channel.addElement("language");
        language.setText((String) map.get("language"));
        Element item = channel.addElement("item");
        generateDocumentItem(item, (Map<String, Object>) map.get("item"));
    }

    /**
     * 构建item子节点
     *
     * @param item    item节点
     * @param itemMap item节点的填充值
     */
    private static void generateDocumentItem(Element item, Map<String, Object> itemMap) {
        Element title = item.addElement("title");
        title.setText((String) itemMap.get("title"));
        Element link = item.addElement("link");
        link.setText((String) itemMap.get("link"));
        Element description = item.addElement("description");
        description.addCDATA((String) itemMap.get("description"));
        Element source = item.addElement("source");
        source.setText((String) itemMap.get("source"));
        Element pubDate = item.addElement("pubDate");
        pubDate.setText(DateUtil.formatDateTime(Objects.isNull(itemMap.get("pubDate")) ? new Date() : (Date) itemMap.get("pubDate")));
    }

    public static void main(String[] args) {
        Map<String, Object> itemMap = new HashMap<>();
        itemMap.put("title", "标题");
        itemMap.put("link", "https://www.baidu.com/");
        itemMap.put("description", "123456789");
        itemMap.put("source", "qaz");
        appendDocumentItem("./files/xml/rss.xml", itemMap);
//        System.out.println(createXml("./files/xml/rss.xml", null));
    }
}
