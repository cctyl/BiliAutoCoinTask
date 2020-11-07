package cn.tyl.bilitask.task.impl;

import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.task.Task;
import cn.tyl.bilitask.utils.BiliVideoUtils;
import cn.tyl.bilitask.utils.RequestUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;

@Component
@Slf4j
public class NewDailyCoinTask implements Task {


    @Autowired
    BiliVideoUtils biliVideoUtils;

    @Override
    public void run() {
        //0.还剩下多少硬币
        int myCoin = biliVideoUtils.getCoin();
        if (myCoin < 1) {
            log.info("没硬币了，今日不投币");
        }
        //1.今天获得了多少投币经验
        Integer reward = biliVideoUtils.getReward();
        //2.今天还需要投多少个硬币
        Integer remainCoin = (50 - reward) / 10;

        //2.1硬币还足够投吗？ 实际投币数
        Integer actualCoin = (myCoin > remainCoin ? remainCoin : myCoin);

        //3.今天要给哪些视频投币,防止有些视频已经投币，所以多预留一些视频
        List<HistoryList> history = biliVideoUtils.getHistory(actualCoin + 5);

        int index = 0;
        int throwNum = 0;
        //4.执行投币任务
        while (true) {
            //如果拿到的视频都投失败了，那停止投币
            if (index == (actualCoin + 5)) {
                break;

            }
            //判断投的硬币数是否达到了实际可投数
            if (throwNum >= actualCoin) {
                //达到了停止投币
                break;
            }
            String oid = history.get(index).getHistory().getOid();
            if (StringUtils.isEmpty(oid)) {
                //为空抛异常
                throw new RuntimeException("获取的视频id居然为空，赶快检查！");
            }
            //不为空，继续执行投币任务
            boolean result = biliVideoUtils.throwCoin(oid, "1", "1");

            if (result) {
                //投币成功，投下一个视频，并且投币数+1
                index++;
                throwNum++;
            } else {
                //投币失败，跳过当前视频，投下一个视频。已投硬币数不增加
                index++;
            }
        }

        log.info("今日尝试投币"+index+"次，成功投出"+throwNum+"个硬币，投币失败"+(index-throwNum));

    }


}
