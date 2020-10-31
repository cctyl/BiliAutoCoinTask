package cn.tyl.bilitask.task;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RData;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.utils.Request;
import cn.tyl.bilitask.utils.RequestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;


/**
 * 完成B站每日任务
 *
 * @author srcrs
 * @Time 2020-10-13
 */
@Component
@Slf4j
public class DailyTask {

    /**
     * 获取DATA对象
     */
    @Autowired
    Data data;

    @Autowired
    Request request;

    @Autowired
    RequestUtil requestUtil;


    @Autowired
    ObjectMapper objectMapper;

    public void run() {
        try {
            JSONArray regions = getRegions("6", "1");
            JSONObject report = report(regions.getJSONObject(5).getString("aid"), regions.getJSONObject(5).getString("cid"), "300");
            log.info("模拟观看视频 -- {}", "0".equals(report.getString("code")) ? "成功" : "失败");
            JSONObject share = share(regions.getJSONObject(5).getString("aid"));
            log.info("分享视频 -- {}", "0".equals(share.getString("code")) ? "成功" : "失败");
        } catch (Exception e) {
            log.error("每日任务异常 -- " + e);
        }
    }

    /**
     * 获取硬币的剩余数
     *
     * @return Integer
     * @author srcrs
     * @Time 2020-10-13
     */
    public Integer getCoin() {
        JSONObject jsonObject = request.get("https://api.bilibili.com/x/web-interface/nav?build=0&mobi_app=web");
        int money = (int) (Double.parseDouble(jsonObject.getJSONObject("data").getString("money")));
        return money;
    }


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

    /**
     * 获取B站推荐视频
     *
     * @param ps  代表你要获得几个视频
     * @param rid B站分区推荐视频
     * @return JSONArray
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONArray getRegions(String ps, String rid) {
        String params = "?ps=" + ps + "&rid=" + rid;
        JSONObject jsonObject = request.get("https://api.bilibili.com/x/web-interface/dynamic/region" + params);
        //jsonArray实际就是视频列表
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("archives");
        JSONArray jsonRegions = new JSONArray();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONObject cache = new JSONObject();
            //下面的代码，本质就是取出视频中需要的信息，存到cache中，在存到jsonRegions中
            cache.put("title", json.getString("title"));
            cache.put("aid", json.getString("aid"));
            cache.put("bvid", json.getString("bvid"));
            cache.put("cid", json.getString("cid"));
            jsonRegions.add(cache);
        }
        return jsonRegions;
    }

    /**
     * 模拟观看视频
     *
     * @param aid     视频 aid 号
     * @param cid     视频 cid 号
     * @param progres 模拟观看的时间
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject report(String aid, String cid, String progres) {
        String body = "aid=" + aid
                + "&cid=" + cid
                + "&progres=" + progres
                + "&csrf=" + data.getBili_jct();
        JSONObject post = request.post("http://api.bilibili.com/x/v2/history/report", body);

        return post;
    }

    /**
     * 分享指定的视频
     *
     * @param aid 视频的aid
     * @return JSONObject
     * @author srcrs
     * @Time 2020-10-13
     */
    public JSONObject share(String aid) {
        String body = "aid=" + aid + "&csrf=" + data.getBili_jct();
        JSONObject post = request.post("https://api.bilibili.com/x/web-interface/share/add", body);
        return post;
    }
}
