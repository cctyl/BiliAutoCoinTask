package cn.tyl.selenium;

import cn.tyl.dao.FavlistDAO;
import cn.tyl.dao.HistoryDAO;
import cn.tyl.dao.impl.FavlistDAOImpl;
import cn.tyl.dao.impl.HistoryDAOImpl;
import cn.tyl.domain.Favlist;
import cn.tyl.domain.HistoryVideo;
import cn.tyl.utils.BiliUtils;
import cn.tyl.utils.SeleniumUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.dao.EmptyResultDataAccessException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 0.准备好数据库中的数据，初始化webdriver
 * <p>
 * 1.从数据库中获取未投币的数据
 * 2.完成登录操作
 * 3.执行投币功能
 * 能投币，投币完把coin_tag改为1
 * 已经投币，把coin_tag改为1
 * 4.退出浏览器
 * 5.定时更新数据库中数据
 */
public class BiliAutoCoin {


    public Logger log4j = Logger.getLogger(BiliAutoCoin.class);

    public int reAutoCount = 0;

    public void auto() {

        log4j.info("调用了auto方法");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        WebDriver driver = new ChromeDriver(options);

        BiliUtils.login(driver);
        try {
            FavlistDAO dao = new FavlistDAOImpl();
            Favlist video = dao.getVideo();//拿到要投币的视频
            log4j.info("今天要投币的视频是" + video.getVideo_title());

            for (int i = 0; i < 2; i++) {

                boolean b = false; //默认这个视频没投币成功
                int count = 0; //用于判断本次投币是否为重投，方便写日志
                do {

                    if (count > 0) {
                        //重投

                        log4j.info("换一个视频投币");
                        video = dao.getVideo();
                        log4j.info("今天要投币的视频是" + video.getVideo_title());
                    }


                    b = BiliUtils.biliCoin(driver, video);//执行投币操作

                    System.out.println(video.getCoin_tag());


                    //把投币后收集到的信息写入数据库
                    dao.updateCoin_tag(video);

                    count++;
                    //投币失败就循环再投


                } while (!b);


            }

            Date today = new Date();
            String fomatStr = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat format = new SimpleDateFormat(fomatStr);
            String nowTime = format.format(today);
            log4j.info(nowTime + "--完成今日投币任务");


        } catch (InterruptedException e) {
            log4j.info(e.getMessage());
        } catch (EmptyResultDataAccessException e) {

            changeToHistory(driver);

        } finally {

            SeleniumUtil.quit(driver);
        }


       /* public void test () throws InterruptedException {

            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--user-data-dir=/home/bim/.config/google-chrome/Default");
            WebDriver driver = new ChromeDriver(chromeOptions);


            driver.get("https://t.bilibili.com/");


            Thread.sleep(3000);

            driver.quit();

        }
*/

    }

    /**
     * 收藏夹中没有数据了，改为投历史记录的硬币
     */
    public void changeToHistory(WebDriver driver) {


        //如果收藏夹中没有视频了，开始从历史记录列表中拿出视频投币，数据库就不由这个程序来更新了
        log4j.info("收藏夹中没有数据，开始从历史记录中获取数据");
        HistoryDAO dao = new HistoryDAOImpl();
        HistoryVideo historyVideo = dao.getHistoryVideo();//拿到要投币的视频
        log4j.info("今天要投币的视频是" + historyVideo.getVideo_title());

        for (int i = 0; i < 2; i++) {

            boolean b = false; //默认这个视频没投币成功
            int count = 0; //用于判断本次投币是否为重投，方便写日志
            do {

                if (count > 0) {
                    //重投

                    log4j.info("换一个视频投币");
                    historyVideo = dao.getHistoryVideo();
                    log4j.info("今天要投币的视频是" + historyVideo.getVideo_title());
                }


                try {
                    b = BiliUtils.biliCoin(driver, historyVideo);//执行投币操作
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }


                //把投币后收集到的信息写入数据库
                dao.updateCoin_tag(historyVideo);

                count++;
                //投币失败就循环再投


            } while (!b);


        }


        Date today = new Date();
        String fomatStr = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(fomatStr);
        String nowTime = format.format(today);
        log4j.info(nowTime + "--完成今日投币任务");

    }

}