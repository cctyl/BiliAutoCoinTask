package cn.tyl.bilitask.utils;

import cn.tyl.bilitask.entity.Data;
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

    @Autowired
    Data data;

    /**
     * 进行直播签到
     *1
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



    /**
     * 获取银瓜子的数量
     * @return Integer
     * @author tyl
     * @Time 2020-11-8
     */
    public Integer getSilver(){

        String get = requestUtil.get("https://api.live.bilibili.com/xlive/web-ucenter/user/get_user_info");
        JsonNode root = null;
        try {
            root = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        int sliver = Integer.parseInt(root.get("data").get("silver").toPrettyString());
        return sliver;
    }



    /**
     * 银瓜子兑换成硬币
     * @return
     * @author tyl
     * @Time 2020-11-8
     */
    public void silver2coin(){
        String body = "csrf_token="+data.getBili_jct();
        String post = requestUtil.post("https://api.live.bilibili.com/pay/v1/Exchange/silver2coin", body);
        JsonNode root = null;
        try {
            root = objectMapper.readTree(post);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String msg = root.get("msg").toPrettyString();
        if (msg.contains("成功")){
            log.info("兑换成功，银瓜子-700，硬币+1");

        }else {
            log.info(msg);
        }



    }



    /**
     * 获取一个直播间的room_id
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public String xliveGetRecommend(){
        String get = requestUtil.get("https://api.live.bilibili.com/relation/v1/AppWeb/getRecommendList");
        JsonNode root = null;
        try {
            root = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String roomid = root.get("data").get("list").get(6).get("roomid").toPrettyString();
        return roomid;

    }


}
