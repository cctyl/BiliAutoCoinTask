package cn.tyl.bilitask.task.impl;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.task.Task;
import cn.tyl.bilitask.utils.RequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 完成每日任务
 */
@Component
@Slf4j
public class NewDailyTask implements Task {

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
     * 获取剩余硬币数
     *
     * @return
     */
    public Double getCoin() {
        String get = requestUtil.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        SimpleResponseEntity simpleResponseEntity = null;
        Double money = 0.0;
        try {
            simpleResponseEntity = objectMapper.readValue(get, SimpleResponseEntity.class);
            LinkedHashMap data = (LinkedHashMap) simpleResponseEntity.getData();
            money = (Double) data.get("money");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return money;
    }


    @Override
    public void run() {
        //获取历史观看视频
        List<HistoryList> dataList = getHistory(6);

        //拿到其中一个视频
        HistoryList historyList = dataList.get(2);

        //对这个视频进行观看
        RespnseEntity report = report(historyList.getHistory().getOid(),
                historyList.getHistory().getCid(), "300");
        log.info("模拟观看视频 -- {}", "0".equals(report.getCode() + "") ? "成功" : "失败");

        //对这个视频进行分享
        SimpleResponseEntity share = share(historyList.getHistory().getOid());
        log.info("分享视频 -- {}", "0".equals(share.getCode() + "") ? "成功" : "失败");

    }
}
