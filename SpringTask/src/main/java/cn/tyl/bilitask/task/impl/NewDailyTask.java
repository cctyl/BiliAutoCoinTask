package cn.tyl.bilitask.task.impl;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.task.Task;
import cn.tyl.bilitask.utils.BiliVideoUtils;
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
    Data data;

    @Autowired
    BiliVideoUtils videoUtils;










    @Override
    public void run() {
        //获取历史观看视频
        List<HistoryList> dataList = videoUtils.getHistory(6);

        //拿到其中一个视频
        HistoryList historyList = dataList.get(2);

        //对这个视频进行观看
        RespnseEntity report = videoUtils.report(historyList.getHistory().getOid(),
                historyList.getHistory().getCid(), "300");
        log.info("模拟观看视频 -- {}", "0".equals(report.getCode() + "") ? "成功" : "失败");

        //对这个视频进行分享
        SimpleResponseEntity share = videoUtils.share(historyList.getHistory().getOid());
        log.info("分享视频 -- {}", "0".equals(share.getCode() + "") ? "成功" : "失败");

    }
}
