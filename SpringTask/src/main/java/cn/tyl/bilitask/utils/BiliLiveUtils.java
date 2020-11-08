package cn.tyl.bilitask.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BiliLiveUtils {


    @Autowired
    RequestUtil requestUtil;


    @Autowired
    ObjectMapper objectMapper;

    /**
     * 进行直播签到
     *
     * @return
     */
    public boolean xliveSign() {


        String get = requestUtil.get("https://api.live.bilibili.com/xlive/web-ucenter/v1/sign/DoSign");

        JsonNode root = null;
        try {
            root = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error("json解析失败，json为{}",get);
        }
        int code = Integer.parseInt(root.get("code").toPrettyString());
        if (code==0){
            String message = root.get("data").get("text").toPrettyString();
            log.info("直播区签到成功，获得{}",message);
            return true;
        }else {
            String errMsg = root.get("message").toPrettyString();
            log.error("直播区签到失败，{}",errMsg);
            return false;
        }

    }

}
