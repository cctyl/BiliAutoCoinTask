package cn.tyl.selenium;

import cn.tyl.dao.HistoryDAO;
import cn.tyl.dao.impl.HistoryDAOImpl;
import cn.tyl.domain.HistoryVideo;
import cn.tyl.utils.BiliUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;

/**
 * 用于收集昨天观看过的视频记录，并存入数据库
 */
public class BiliUpdateHistory {

    public static Logger log4j = Logger.getLogger(BiliUpdateFavlist.class);

    public static void main(String[] args) {
        WebDriver driver = null;
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--user-data-dir=/home/tyl/.config/google-chrome/Default");
        try {

            log4j.info("开始更新观看历史记录的数据库");
            driver = new ChromeDriver();

            log4j.info("准备执行BiliUtils.collectHistoryVideo方法");

            ArrayList<HistoryVideo> historyVideos = BiliUtils.collectHistoryVideo(driver);
            int updateNum = historyVideos.size();

            //处理后的favLists就是不包含重复的
            //把处理后的数据写入数据库
            log4j.info("本次找到的新视频共有" + updateNum + "条，准备写入数据库");
            HistoryDAO dao = new HistoryDAOImpl();
            dao.saveVideoInfo(historyVideos);



        } catch (Exception e) {
            log4j.error("出错了，错误为-----" + e.getMessage());
        } finally {
            if (driver != null) {
                log4j.info("准备退出浏览器");
                driver.quit();

            }
        }


    }


}
