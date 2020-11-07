package cn.tyl.bilitask.utils;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import com.fasterxml.jackson.core.JsonProcessingException;
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
}
