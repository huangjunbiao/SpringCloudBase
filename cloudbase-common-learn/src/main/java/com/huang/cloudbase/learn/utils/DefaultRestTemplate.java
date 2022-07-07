package com.huang.cloudbase.learn.utils;

import cn.hutool.core.lang.Assert;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author huangjunbiao_cdv
 */
@Component
public class DefaultRestTemplate {

    public static final String DIRECTORY = System.getProperty("java.io.tmpdir");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * post请求，body参数
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 返回数据
     */
    public Map<String, Object> postWithBodyParam(String url, Map<String, Object> json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return ObjectMapperUtil.readValueAsMap(exchange.getBody());
    }

    /**
     * post请求，url参数，ContentType=MULTIPART_FORM_DATA
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 返回数据
     */
    public Map<String, Object> postWithUrlParam(String url, Map<String, Object> json) {
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.setAll(json);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> exchange = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return ObjectMapperUtil.readValueAsMap(exchange.getBody());
    }

    /**
     * post请求，url参数，ContentType=APPLICATION_FORM_URLENCODED
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 返回数据
     */
    public Map<String, Object> postWithParam(String url, Map<String, Object> json) {
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.setAll(json);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> exchange = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return ObjectMapperUtil.readValueAsMap(exchange.getBody());
    }

    /**
     * get方式请求，url参数，APPLICATION_JSON
     *
     * @param url  请求地址
     * @param json 请求参数
     * @return 请求结果信息Map
     */
    public Map<String, Object> get(String url, Map<String, Object> json) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, Object> stringObjectEntry : json.entrySet()) {
            builder.queryParam(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> exchange = this.restTemplate.exchange(builder.build().encode()
                    .toUri(), HttpMethod.GET, requestEntity, String.class);
            if (exchange.getStatusCode().equals(HttpStatus.OK)) {
                // 接口调用成功
                return ObjectMapperUtil.readValueAsMap(exchange.getBody());
            } else {
                //接口调用返回错误
                return null;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * get请求，获取数据转换为对应的java class
     *
     * @param url 请求地址
     * @param param url请求参数
     * @param className 类名
     * @return 数据信息
     */
    public Object getForClass(String url, Map<String, Object> param, String className) {
        Object object = null;
        try {
            object = this.restTemplate.getForObject(HttpUtil.generateUrl(url, param), Class.forName(className));
        } catch (ClassNotFoundException e) {
            logger.warn("请求数据失败，找不到类{}", className, e);
        }
        return object;
    }

    /**
     * post请求，ContentType=APPLICATION_JSON
     *
     * @param url 请求地址
     * @param urlParam  地址参数
     * @param body  body请求体参数
     * @return 返回信息Map
     */
    public Map<String, Object> post(String url, Map<String, Object> urlParam, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> exchange = this.restTemplate.exchange(HttpUtil.generateUrl(url, urlParam), HttpMethod.POST, requestEntity, String.class);
        return ObjectMapperUtil.readValueAsMap(exchange.getBody());
    }

    /**
     * post请求获取返回数据，返回类型对应定义的java class
     *
     * @param url       地址
     * @param json      body参数
     * @param className 类名
     * @return 返回数据
     */
    public Object postForClass(String url, Map<String, Object> json, String className) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return ObjectMapperUtil.readValueToJavaObject(exchange.getBody(), className);
    }

    /**
     * 从公有云下载图片到本地
     *
     * @param url 公有云请求地址
     * @return 在本地创建文件后的路径
     */
    public File getImageResourceByUrl(String url) {
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
        byte[] body = responseEntity.getBody();
        File tempFile = null;
        FileOutputStream fileOutputStream = null;
        String prefix = url.substring(url.lastIndexOf("/", url.lastIndexOf("/") - 1) + 1, url.lastIndexOf("/"));
        String suffix = url.substring(url.lastIndexOf("."));
        try {
            tempFile = File.createTempFile(prefix, suffix, new File(DIRECTORY));
            fileOutputStream = new FileOutputStream(tempFile);
            assert (null != body);
            fileOutputStream.write(body);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                if (null != fileOutputStream) fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Assert.notNull(tempFile, "创建临时文件失败");
        return tempFile;
    }

    /**
     * 获取网络视频下载到本地
     *
     * @param videoUrl 网路视频的地址
     * @return 下载完成后的地址
     */
    public File getVideoByResourceUrl(String videoUrl) {
        File tempFile = null;
        String prefix = videoUrl.substring(videoUrl.lastIndexOf("/", videoUrl.lastIndexOf("/") - 1) + 1, videoUrl.lastIndexOf("/"));
        String suffix = videoUrl.substring(videoUrl.lastIndexOf("."));
        try {
            URL httpUrl = new URL(videoUrl);
            tempFile = File.createTempFile(prefix, suffix, new File(DIRECTORY));
            FileUtils.copyURLToFile(httpUrl, tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile;
    }

    /**
     * 文件File类型转byte[]
     *
     * @param filePath 文件路径
     * @return 返回的二进制字节流
     */
    public byte[] file2Byte(String filePath) {
        byte[] fileBytes = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;

    }

    /**
     * 文件File类型转byte[]
     *
     * @param file 文件
     * @return 返回的二进制字节流
     */
    public byte[] file2Byte(File file) {
        byte[] fileBytes = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;

    }

    public static void main(String[] args) {
        /*DefaultRestTemplate defaultRestTemplate = new DefaultRestTemplate();
        defaultRestTemplate.restTemplate = new RestTemplate();
        Map<String, Object> maps = new HashMap<>();
        maps.put("access_token", "2.00P8xeFEuxgtcD25746c778b08HyMc");
        maps.put("rip", "223.72.191.2");
        maps.put("status", "分享一下");
        Map<String, Object> objectMap = defaultRestTemplate
                                       .postWithParam("https://api.weibo.com/2/statuses/share.json", maps);
        System.out.println(objectMap);

        Map<String, Object> json3 = new HashMap<>();
        json3.put("app_id", "1663579668130728");
        json3.put("article_id", "1735487200103027133");
        json3.put("app_token", "6fb16dce32214a4149f05b96e6e05652");
        Map<String, Object> map = defaultRestTemplate
                                      .postWithUrlParam("https://baijiahao.baidu.com/builderinner/open/resource/query/status", json3);
        Map<String, Object> data = (Map<String, Object>) map.get("data");
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            System.out.println(entry.getKey());
            Map<String, Object> value = (Map<String, Object>) entry.getValue();
            System.out.println(value.get("status"));
            if (Objects.equals(value.get("status"), "publish")) {

            } else if (Objects.equals(value.get("status"), "audit")) {

            }
        }*/

        /*System.out.println(WxStatisticResult.class.getSimpleName());
        System.out.println(WxStatisticResult.class.getName());
        try {
            System.out.println(Class.forName(WxStatisticResult.class.getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (true) {
            return;
        }*/
        /*DefaultRestTemplate defaultRestTemplate = new DefaultRestTemplate();
        defaultRestTemplate.restTemplate = new RestTemplate();
        Map<String, Object> body = new HashMap<>();
        body.put("begin_date", "2022-05-26");
        body.put("end_date", "2022-05-26");
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", "57_cLJoO4fFBBIIlSZy-FnyR9-NfeI8fQRP_hiLq772ajqw1karh4TuPEixZaPX-y1F1rq4dQySvWhQkOLkSOWv3SgoKscJbgJMtJS905F0SwTd2rUxH6z5Sp3gbtcfEEfAhfc8akxX6HT-P-dZBCYeADAGHD");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        Object post = defaultRestTemplate.post(HttpUtil
                                                   .generateUrl("https://api.weixin.qq.com/datacube/getarticletotal", params), body, WxStatisticResult.class.getName());
        System.out.println(post);


        ResponseEntity<WxStatisticResult> exchange = defaultRestTemplate.restTemplate.exchange(HttpUtil.generateUrl("https://api.weixin.qq.com/datacube/getarticletotal", params),
            HttpMethod.POST, requestEntity, new ParameterizedTypeReference<WxStatisticResult>() {});
        System.out.println(exchange.getBody().getList());


        Map<String, Object> map = defaultRestTemplate
                                       .postWithBodyParam(HttpUtil.generateUrl("https://api.weixin.qq.com/datacube/getarticletotal", params), body);

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            System.out.println(entry.getValue());
        }*/
    }
}
