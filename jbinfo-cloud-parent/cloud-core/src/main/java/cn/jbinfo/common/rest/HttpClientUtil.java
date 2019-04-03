package cn.jbinfo.common.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaobin on 2017/8/15.
 */
public class HttpClientUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * get请求方式
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGetByParams(String url) throws HttpException, IOException {
        HttpClient httpClient = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient();
        HttpUriRequest request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        return handlerResponse(response);
    }

    /**
     * 功能:httpsGet请求
     *
     * @param url
     * @return
     */
    public static String httpsGet(String url) {
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient().execute(get);
            return handlerResponse(response);
        } catch (IOException e) {
            LOGGER.error("httpsGet [" + url + "] exception ", e);
        }
        return null;
    }

    /**
     * 功能:httpsPost请求
     *
     * @param url
     * @return
     */
    public static String httpsPost(String url) {
        try {
            HttpPost post = new HttpPost(url);
            HttpResponse response = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient().execute(post);
            return handlerResponse(response);
        } catch (IOException e) {
            LOGGER.error("httpsGet [" + url + "] exception ", e);
        }
        return null;
    }

    private static HttpPost getPostMethod(String url) {
        HttpPost post = new HttpPost(url);
        RequestConfig config = RequestConfig.DEFAULT;
        post.setConfig(config);
        return post;
    }

    public static String sendPostByMap(String url, Map<String, Object> paramMap) throws HttpException, IOException {
        if (url == null || null == paramMap)
            return null;
        HttpPost post = getPostMethod(url);
        HttpResponse response = sendHandler(null, paramMap, post);
        return handlerResponse(response);
    }

    private static HttpResponse sendHandler(Map<String, String> headMap, Map<String, Object> paramMap,
                                            HttpEntityEnclosingRequestBase reqBase) throws IOException, ClientProtocolException {
        HttpClient httpClient = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient();
        ; // 构造HttpClient的实例
        //头信息
        if (headMap != null)
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                reqBase.addHeader(entry.getKey(), entry.getValue());
            }
        if (paramMap != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator<String> iter = paramMap.keySet().iterator(); iter.hasNext(); ) {
                String key = iter.next();
                nvps.add(new BasicNameValuePair(key, MapUtils.getString(paramMap, key)));
            }
            reqBase.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
        }
        HttpResponse response = httpClient.execute(reqBase);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            LOGGER.error("sendHandler failed: " + response.getStatusLine());
        }
        return response;
    }

    /**
     * get请求方式,返回状态编码
     *
     * @param url
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static int sendGet(String url) {
        if (StringUtils.isEmpty(url))
            return 0;
        HttpClient httpClient = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient(); // 构造HttpClient的实例
        HttpUriRequest request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            return 500;
        }
    }

    /**
     * get请求方式，参数追加在url后边
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String sendGetByParams(String url, String params) throws HttpException, IOException {
        HttpClient httpClient = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient(); // 构造HttpClient的实例
        if (url.lastIndexOf("?") != -1) {
            url += "&" + params;
        } else
            url += "?" + params;
        HttpUriRequest request = new HttpGet(url);
        HttpResponse response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            LOGGER.error("sendGetByParams failed: " + response.getStatusLine());
        }
        return handlerResponse(response);

    }

    /**
     * get请求方式带头信息，参数追加在url后边
     *
     * @param url
     * @param paramMap 参数
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String sendGetByParams(String url, Map<String, String> paramMap) {
        try {
            HttpClient httpClient = HttpPoolClientBuilder.getHttpClientFactory().getHttpClient(); // 构造HttpClient的实例
            StringBuilder params = getUrlParam(paramMap);
            if (url.lastIndexOf("?") != -1) {
                url += "&" + params.toString();
            } else
                url += "?" + params.toString();
            HttpUriRequest request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("sendGetByParams failed: " + response.getStatusLine());
            }
            return handlerResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static StringBuilder getUrlParam(Map<String, String> paramMap) {
        if (paramMap == null) return new StringBuilder("");
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            //String value = StringUtils.urlEncode(entry.getValue());
            params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        //删除最后一个&字符
        if (params.length() > 1) {
            params.deleteCharAt(params.length() - 1);
        }
        return params;
    }

    private static String handlerResponse(HttpResponse response) throws IOException {
        try {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } finally {
            //释放关闭连接
            EntityUtils.consume(response.getEntity());
        }
    }

    public static String sendGetByRest(String url, Map<String, String> headMap)
            throws HttpException, IOException {
        HttpHeaders header = new HttpHeaders();
        //头信息
        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                header.set(entry.getKey(), entry.getValue());
            }
        }
        header.set("Content-Type", "application/json;charset=UTF-8");
        return RestClient.exchange(url, HttpMethod.GET, header, "", String.class);
    }


    public static String sendPostByMapRest(String url, Map<String, String> headMap, Map<String, String> paramMap)
            throws HttpException, IOException {
        if (url == null || null == paramMap)
            return null;
        HttpHeaders header = new HttpHeaders();
        //头信息
        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                header.set(entry.getKey(), entry.getValue());
            }
        }
        header.set("Content-Type", "application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String paramStr = mapper.writeValueAsString(paramMap);
        return RestClient.exchange(url, HttpMethod.POST, header, paramStr, String.class);
    }

    public static String sendPostByRest(String url, Map<String, String> headMap, Object object)
            throws HttpException, IOException {
        if (url == null)
            return null;
        HttpHeaders header = new HttpHeaders();
        //头信息
        if (headMap != null) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                header.set(entry.getKey(), entry.getValue());
            }
        }
        header.set("Content-Type", "application/json;charset=UTF-8");
        if (object != null) {
            ObjectMapper mapper = new ObjectMapper();
            String paramStr = mapper.writeValueAsString(object);
            return RestClient.exchange(url, HttpMethod.POST, header, paramStr, String.class);
        }
        return RestClient.exchange(url, HttpMethod.POST, header, Maps.newConcurrentMap(), String.class);
    }


}
