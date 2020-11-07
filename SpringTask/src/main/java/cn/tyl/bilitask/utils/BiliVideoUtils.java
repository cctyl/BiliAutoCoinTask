package cn.tyl.bilitask.utils;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
@Slf4j
public class BiliVideoUtils {

    @Autowired
    RequestUtil requestUtil;


    @Autowired
    ObjectMapper objectMapper;


    @Autowired
    Data data;

    /**
     * 获取历史观看视频
     * api.bilibili.com/x/web-interface/history/cursor?type=archive&ps=20
     *
     * @param ps 数量
     * @return
     * @author tyl
     * @Time 2020-10-31
     */
    public List<HistoryList> getHistory(int ps) {

        String params = "?ps=" + ps + "&type=archive";
        String s = requestUtil.get("https://api.bilibili.com/x/web-interface/history/cursor" + params);
        if (StringUtils.isEmpty(s)) {
            throw new RuntimeException("响应为空！");
        }
        RespnseEntity respnseEntity = null;
        try {
            respnseEntity = objectMapper.readValue(s, RespnseEntity.class);
        } catch (JsonProcessingException e) {
            log.info("json转换失败");
        }

        // 通用
        return respnseEntity.getData().getList();
    }



    /**
     * 模拟观看视频
     *
     * @param aid
     * @param cid
     * @param progres
     * @return
     */
    public RespnseEntity report(String aid, String cid, String progres) {
        String body = "aid=" + aid
                + "&cid=" + cid
                + "&progres=" + progres
                + "&csrf=" + data.getBili_jct();
        String post = requestUtil.post("http://api.bilibili.com/x/v2/history/report", body);

        RespnseEntity respnseEntity = null;
        try {
            respnseEntity = objectMapper.readValue(post, RespnseEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        return respnseEntity;
    }


    /**
     * 分享指定的视频
     *
     * @param aid 视频的aid
     * @return SimpleResponseEntity
     * @author tyl
     * @Time 2020-10-31
     */
    public SimpleResponseEntity share(String aid) {
        String body = "aid=" + aid + "&csrf=" + data.getBili_jct();
        String post = requestUtil.post("https://api.bilibili.com/x/web-interface/share/add", body);
        SimpleResponseEntity respnseEntity = null;
        try {
            respnseEntity = objectMapper.readValue(post, SimpleResponseEntity.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return respnseEntity;
    }


    /**
     * 获取已投币数
     *
     * @Auth tyl
     * @Date 2020-11-7
     * @return
     */
    public Integer getReward() {
        String get = requestUtil.get("https://account.bilibili.com/home/reward");
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error("json转换异常，检查getReward（）方法-----" + e.getMessage());
        }
        String coinString = jsonNode.get("data").get("coins_av").toPrettyString();
        return Integer.parseInt(coinString);
    }


    /**
     * 获取剩余硬币数
     * 默认向下取整，去除小数
     *
     * @return
     * @Auth tyl
     * @Date 2020-11-7
     */
    public int getCoin() {
        String get = requestUtil.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        JsonNode root = null;
        try {
            root = objectMapper.readTree(get);
        } catch (JsonProcessingException e) {
            log.error("json转换异常，检查getCoin方法-----" + e.getMessage());
        }
        String lastCoin = root.get("data").get("money").toPrettyString();

        return (int)Double.parseDouble(lastCoin);
    }



    /**
     * 给视频投币
     *
     * @param aid         视频 aid 号
     * @param num         投币数量
     * @param select_like 是否点赞
     * @return 投币结果
     * @author tyl
     * @Time 2020-11-7
     */
    public boolean throwCoin(String aid, String num, String select_like) {

        String body = "aid=" + aid
                + "&multiply=" + num
                + "&select_like=" + select_like
                + "&cross_domain=" + "true"
                + "&csrf=" + data.getBili_jct();
        String post = requestUtil.post("https://api.bilibili.com/x/web-interface/coin/add", body);
        JsonNode root = null;
        try {
            root = objectMapper.readTree(post);
        } catch (JsonProcessingException e) {
            log.error("json转换异常，检查getCoin方法-----" + e.getMessage());
        }
        String code = root.get("code").toPrettyString();
        String message = root.get("message").toPrettyString();
        if (code.equals("0")){
            return true;
        }else {

            log.error("aid："+aid+" 投币失败,message:"+message);
            return false;
        }

    }
}
