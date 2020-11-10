package cn.tyl.bilitask.schedule;

import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.RespnseEntity;
import cn.tyl.bilitask.entity.response.SimpleResponseEntity;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.utils.BiliLiveUtils;
import cn.tyl.bilitask.utils.BiliVideoUtils;
import cn.tyl.bilitask.utils.MessageUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;


/**
 * 定时任务的调用类
 */
@Component
@Slf4j
public class TaskSchedule implements CommandLineRunner {


    @Autowired
    Data data;


    @Autowired
    BiliVideoUtils biliVideoUtils;


    @Autowired
    BiliLiveUtils biliLiveUtils;

    @Autowired
    MessageUtils messageUtils;

    /**
     * 启动任务
     * 每天早上6点01分01秒执行一次
     */
//    @Scheduled(cron = "1 50 12 * * *")
    public void run() {

        //1.投币
        String s1 = throwCoinTask();

        //2.模拟观看视频，分享
        String s2 = viewAndShareTask();


        //3.直播区签到
        String s3 = liveSignEveryDay();

        //4.银瓜子转换硬币
        String s4 = sliverToCoin();

        //5.直播间送出到期礼物
        String s5 = giveGift();
        //99.发送消息，表明今日任务执行完毕 分隔符

        String total = s1 + "\n" + s2 + "\n" + s3 + "\n" + s4 +"\n"+s5;
        String today = LocalDate.now().toString();

        messageUtils.sendMessage(today + "【任务执行情况报告】", total);
    }


    /**
     * 随机抽一个直播间送出礼物
     * @return
     */
    public String giveGift() {

        String taskMessage = "";
        //获取一个直播间的room_id
        String roomId = biliLiveUtils.getliveRecommend();
        //通过room_id获取uid
        String uid = biliLiveUtils.getRoomUid(roomId);
        // B站后台时间戳为10位
        long nowTime = System.currentTimeMillis() / 1000;
        //获得礼物列表
        JsonNode liveGiftBagList = biliLiveUtils.getLiveGiftBagList();

        //送出状态的标记
        boolean sendFlag = false;
        int count = 0;
        for (JsonNode node : liveGiftBagList) {

            long expireAt = Long.valueOf(node.get("expire_at").toPrettyString());
            // 礼物还剩1天送出,其余保留
            // 永久礼物到期时间为0
            if ((expireAt - nowTime) < 87000 && expireAt != 0) {

                //开始赠送礼物
                JsonNode result = biliLiveUtils.sendLiveGift(roomId, uid, node.get("bag_id").toPrettyString(),
                        node.get("gift_id").toPrettyString(), node.get("gift_num").toPrettyString(), "0", "0", "pc");

                //判断礼物送出结果
                if ("0".equals(result.get("code").toPrettyString())) {
                    //送出成功
                    //礼物的名字
                    String giftName = result.get("data").get("gift_name").toPrettyString();
                    //礼物的数量
                    String giftNum = result.get("data").get("gift_num").toPrettyString();
                    log.info("送礼物给{} {} 数量: {}", roomId, giftName, giftNum);

                    //标记本轮送出状态
                    sendFlag = true;
                    count++;
                } else {
                    //送出失败
                    log.error("礼物送出失败 -- " + result.toPrettyString());
                }
            }
        }

        if (sendFlag){

            taskMessage +="成功送出礼物，送出"+count+"次";
        }else {
            taskMessage+="没有礼物即将到期，所以本次没有送出礼物";
        }

        return taskMessage;

    }


    /**
     * 投币任务
     */
    public String throwCoinTask() {
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

            //拿到要投币的视频id
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

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String taskMessage = "今日尝试投币" + index + "次，成功投出" + throwNum + "个硬币，投币失败" + (index - throwNum);
        log.info(taskMessage);
        return taskMessage;
    }


    /**
     * 模拟观看和模拟分享任务
     */
    public String viewAndShareTask() {

        String taskMessage = "";

        //获取历史观看视频
        List<HistoryList> dataList = biliVideoUtils.getHistory(6);

        //拿到其中一个视频
        HistoryList historyList = dataList.get(2);

        //对这个视频进行观看
        RespnseEntity report = biliVideoUtils.report(historyList.getHistory().getOid(),
                historyList.getHistory().getCid(), "300");
        boolean reportResult = "0".equals(report.getCode() + "");


        //对这个视频进行分享
        SimpleResponseEntity share = biliVideoUtils.share(historyList.getHistory().getOid());
        boolean shareResult = "0".equals(share.getCode() + "");


        //日志以及相关信息的输出
        taskMessage = "模拟观看视频: " + (reportResult ? "成功" : "失败") + "\n"
                + "分享视频: " + (shareResult ? "成功" : "失败");

        log.info(taskMessage);

        return taskMessage;


    }


    /**
     * 直播区每日签到任务
     */
    public String liveSignEveryDay() {
        boolean b = biliLiveUtils.xliveSign();
        if (b) {
            log.info("直播签到成功");
            return "直播签到成功";
        } else {
            log.error("直播签到失败");
            return "直播签到失败";
        }
    }


    /**
     * 将银瓜子转换成硬币，每天只能转换一个硬币
     */
    public String sliverToCoin() {

        //获取剩余银瓜子数
        Integer silver = biliLiveUtils.getSilver();
        if (silver < 700) {
            log.info("银瓜子数量小于700，不兑换");
            return "银瓜子数量小于700，不兑换";
        }

        boolean result = biliLiveUtils.silver2coin();
        if (result) {
            return "硬币兑换成功";
        } else {
            return "不明原因兑换失败";
        }

    }


    @Override
    public void run(String... args) throws Exception {
        run();
    }

}
