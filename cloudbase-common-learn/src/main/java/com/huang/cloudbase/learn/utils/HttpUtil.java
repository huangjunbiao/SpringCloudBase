package com.huang.cloudbase.learn.utils;

import java.util.Map;

/**
 * used for http request
 *
 * @author huangjunbiao_cdv
 */
public class HttpUtil {
    /**
     * 构造参数地址
     *
     * @param baseUrl   原地址
     * @param urlParams 地址请求参数
     * @return 参数请求地址
     */
    public static String generateUrl(String baseUrl, Map<String, Object> urlParams) {
        String url = baseUrl;
        if (urlParams != null && urlParams.size() > 0) {
            url = url + "?";
            for (String key : urlParams.keySet()) {
                url = url + key + "=" + urlParams.get(key) + "&";
            }
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }
}
