package com.huang.cloudbase.learn.document;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.nio.charset.Charset;

public class JsonHandler {
    public static void main(String[] args) {
        readJson();
    }

    public static void readJson() {
        File file = new File("./files/document/json/test.json");
        String s = FileUtil.readString(file, Charset.defaultCharset());
        System.out.println(s);
    }
}
