package cn.tyl.bilitask.task.impl;

import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.task.Task;
import cn.tyl.bilitask.utils.RequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
@Slf4j
public class NewDailyCoinTask implements Task {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestUtil requestUtil;

    @Override
    public void run() {
        //0.还剩下多少硬币

        //1.今天获得了多少投币经验
        Integer reward = getReward();
        //2.今天还需要投多少个硬币
        Integer remainCoin = (50-reward)/10;
        //3.今天要给哪些视频投币

        //4.执行投币任务


    }


    /**
     * 获取已投币数
     * @return
     */
    public Integer getReward() {
        String get = requestUtil.get("https://account.bilibili.com/home/reward");
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error("json转换异常，检查getReward（）方法-----"+e.getMessage());
        }
        String coinString = jsonNode.get("data").get("coins_av").toPrettyString();

        return Integer.parseInt(coinString);
    }



    /**
     * 获取剩余硬币数
     * @Auth tyl
     * @Date 2020-11-7
     * @return
     */
    public Double getCoin() {



        String get = requestUtil.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        JsonNode root = null;
        try {
            root = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error("json转换异常，检查getCoin方法-----"+e.getMessage());
        }
        String lastCoin = root.get("data").get("money").toPrettyString();

        return Double.parseDouble(lastCoin);
    }
}
