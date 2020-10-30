package cn.tyl.bilitask.schedule;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.utils.Request;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 定时任务的调用类
 */
@Component
public class TaskSchedule {


    @Autowired
    Data data;

    @Autowired
    Request request;

    /**
     * 检查用户的状态
     *
     * @return boolean
     * @author tyl
     * @Time 2020-10-30
     */
    public boolean check() {

        JSONObject jsonObject = request.get("https://api.bilibili.com/x/web-interface/nav");
        JSONObject object = jsonObject.getJSONObject("data");
        if ("0".equals(jsonObject.getString("code"))) {
            /** 用户名 */
            data.setUname(object.getString("uname"));
            /** 账户的uid */
            data.setMid(object.getString("mid"));
            /** vip类型 */
            data.setVipType(object.getString("vipType"));
            /** 硬币数 */
            data.setMoney(object.getString("money"));
            /** 经验 */
            data.setCurrentExp(object.getJSONObject("level_info").getString("current_exp"));
            /** 大会员状态 */
            data.setVipStatus(object.getString("vipStatus"));
            /** 钱包B币卷余额 */
            data.setCoupon_balance(object.getJSONObject("wallet").getString("coupon_balance"));
            return true;
        }
        return false;
    }
}
