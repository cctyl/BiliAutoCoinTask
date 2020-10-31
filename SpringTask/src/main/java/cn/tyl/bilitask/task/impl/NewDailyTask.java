package cn.tyl.bilitask.task.impl;

import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.task.Task;
import cn.tyl.bilitask.utils.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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


    /**
     * 获取历史观看视频
     * api.bilibili.com/x/web-interface/history/cursor?type=archive&ps=20
     * @param ps 数量
     * @author tyl
     * @Time 2020-10-31
     * @return
     */
    public List<HistoryList> getHistory(int ps){

        String params = "?ps=" + ps + "&type=archive";
        String s = requestUtil.get("https://api.bilibili.com/x/web-interface/history/cursor" + params);
        if (StringUtils.isEmpty(s)){
            throw new RuntimeException("响应为空！");
        }
        RespnseEntity respnseEntity =null;
        try {
            respnseEntity = objectMapper.readValue(s, RespnseEntity.class);
        } catch (JsonProcessingException e) {
            log.info("json转换失败");
        }

        return respnseEntity.getData().getList();
    }





    @Override
    public void run() {
        List<HistoryList> history = getHistory(5);

    }
}
