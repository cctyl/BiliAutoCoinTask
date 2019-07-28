package cn.tyl.selenium;


import org.apache.log4j.Logger;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BiliTimer  {


    public static Logger log4j = Logger.getLogger(BiliTimer.class);
    /*

    用于完成定时任务


     */


    public static int reAutoCount = 0;// 用于计数

    public static void main(String[] args) {


        log4j.info("开始调用renwu");
        renwu();


    }



        public static void renwu(){
            try {



                Runnable runnable = new Runnable() {
                    public void run() {


                        Date d = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        String time = format.format(d);
                        log4j.info("time ----开始执行定时任务");
                        BiliAutoCoin auto = new BiliAutoCoin();

                        auto.auto();

                        log4j.info("开始休眠，等待明天的任务 ");


                    }
                };


                log4j.info("开始设置定时任务");
                ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                service.scheduleAtFixedRate(runnable, 0, 24, TimeUnit.HOURS);
            } catch (Exception e) {

                log4j.error("出错了" + e.getMessage());


                if (reAutoCount<3){
                    log4j.info("尝试重新启动任务，现在是第"+reAutoCount+"次");
                    renwu();


                }else
                    log4j.info(" 任务重试次数已超时");




            }

    }



}
