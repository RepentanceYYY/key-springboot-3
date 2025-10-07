package com.tairui.function.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tairui.common.core.redis.RedisCache;
import com.tairui.function.domain.UserSubscribe;
import com.tairui.function.mapper.UserSubscribeMapper;
import com.tairui.function.service.IWeChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeChatServer implements IWeChatServer {


    @Value("${wx.token}")
    private String TOKEN;
    // 小程序AppID
    @Value("${wx.appid}")
    private String APPID;
    // 小程序AppSecret
    @Value("${wx.appsecret}")
    private String APPSECRET;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserSubscribeMapper userSubscribeMapper;

    @Override
    public void sendSubscribeMessage(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(message, headers);
            String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + this.getAccessToken();

            String response = restTemplate.postForObject(url, request, String.class);
            System.out.println("微信返回结果: " + response);
            // 可以转成 Map 或者对象
            ObjectMapper mapper = new ObjectMapper();
            Map resultMap = mapper.readValue(response, Map.class);
            Integer errcode = (Integer) resultMap.get("errcode");
            String errmsg = (String) resultMap.get("errmsg");

            if (errcode != 0) {
                throw new RuntimeException("推送失败，微信返回："+response);
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getAccessToken() {
        long now = System.currentTimeMillis();
        /**
         * 如果redis有凭证则直接返回
         */
        Object accessTokenObj = redisCache.getCacheObject("access_token");
        String accessTokenFromRedis = accessTokenObj != null ? accessTokenObj.toString() : null;
        if (accessTokenFromRedis != null) {
            return accessTokenFromRedis;
        }

        String url = UriComponentsBuilder.fromHttpUrl("https://api.weixin.qq.com/cgi-bin/token")
                .queryParam("grant_type", "client_credential")
                .queryParam("appid", APPID)
                .queryParam("secret", APPSECRET)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("获取到access_token" + result);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(result);

            if (jsonNode.has("access_token")) {
                String accessToken = jsonNode.get("access_token").asText();
                /**
                 * 保存100分钟到redis
                 */
                redisCache.setCacheObject("access_token", accessToken, 100, TimeUnit.MINUTES);
                return accessToken;
            } else {
                throw new RuntimeException("获取AccessToken失败" + result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取微信临时凭证access_token后解析返回结果失败" + result);
        }
    }

    @Override
    public String Login(String code) throws JsonProcessingException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID +
                "&secret=" + APPSECRET +
                "&js_code=" + code +
                "&grant_type=authorization_code";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        String openId = jsonNode.get("openid").asText();
        try {
            return openId;
        } catch (Exception e) {
            throw new RuntimeException("解析微信接口返回失败: " + response, e);
        }
    }

    @Override
    public List<UserSubscribe> getUserSubscribeList(UserSubscribe userSubscribe) {
        return userSubscribeMapper.selectUserSubscribeList(userSubscribe);
    }

    @Override
    public int Subscribe(UserSubscribe userSubscribe) {
        userSubscribe.setCreateTime(new Date());
        return userSubscribeMapper.insertUserSubscribe(userSubscribe);
    }
}
