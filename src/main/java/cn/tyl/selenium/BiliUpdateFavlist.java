package cn.tyl.selenium;

import cn.tyl.utils.BiliUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BiliUpdateFavlist {


    public static Logger log4j =  Logger.getLogger(BiliUpdateFavlist.class);
    public static void main(String[] args) {
        WebDriver driver =null;
        ChromeOptions chromeOptions = new ChromeOptions();

        chromeOptions.addArguments("--user-data-dir=/home/tyl/.config/google-chrome/Default");
        try {

            log4j.info("开始更新数据库");
            driver =new ChromeDriver();
            log4j.info("准备执行BiliUtils.updateVideo方法");
            BiliUtils.updateFavlistVideo(driver);
        } catch (Exception e) {
            log4j.error("出错了，错误为-----"+e.getMessage());
        } finally {
            if(driver!=null){
                log4j.info("准备退出浏览器");
                driver.quit();

            }
        }


    }
}
