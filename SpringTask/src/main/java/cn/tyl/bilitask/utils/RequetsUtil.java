package cn.tyl.bilitask.utils;

import cn.tyl.bilitask.entity.Data;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 请求工具类，默认加上cookie
 */
@Component
@Slf4j
public class RequetsUtil {

    @Autowired
    private Data data;


    /**
     * 发起get请求，返回json数据。
     * 只适用于json接口
     * @param url
     * @return
     */
    public String get(String url) {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("connection", "keep-alive");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");
        httpGet.addHeader("referer", "https://www.bilibili.com/");
        httpGet.addHeader("Cookie", data.getCookie());
        HttpResponse resp = null;
        String respContent = null;
        try {
            resp = client.execute(httpGet);
            HttpEntity entity = null;
            if (resp.getStatusLine().getStatusCode() < 400) {
                entity = resp.getEntity();
            } else {
                entity = resp.getEntity();
            }
            respContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            log.info("get请求错误 -- " + e);
        } finally {
            return respContent;
        }
    }

}
