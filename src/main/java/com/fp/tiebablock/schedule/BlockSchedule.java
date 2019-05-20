package com.fp.tiebablock.schedule;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BlockSchedule {

    @Scheduled(cron = "* * * 1 * *")    //  1天封一次
    private void blockId() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        CloseableHttpResponse response = null;
        try {
            HttpPost post = new HttpPost("https://tieba.baidu.com/pmc/blockid");

            Map<String, String> param = new HashMap<>();
            param.put("day", "1");          //  封禁天数
            param.put("fid", "4262092");    // 贴吧的ID -- forum_id
            param.put("ie", "gbk");
            param.put("reason", "测试");
            param.put("tbs", "");           //  它在贴吧回复的主题帖的ID，页面查找tbs
            param.put("portrait[]", "");    //  它在全贴吧的ID

            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                post.setEntity(entity);
            }
            post.setHeader("Host", "tieba.baidu.com");
            post.setHeader("Origin", "https://tieba.baidu.com");
            post.setHeader("Referer", "https://tieba.baidu.com/p/5162834555");
            post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER");
            post.setHeader("X-Requested-With", "XMLHttpRequest");
            post.setHeader("Cookie","");

            response = httpClient.execute(post);
            System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
