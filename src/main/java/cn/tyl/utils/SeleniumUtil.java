package cn.tyl.utils;

import cn.tyl.selenium.BiliTimer2;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumUtil {





    public static Logger log4j = Logger.getLogger(SeleniumUtil.class);

    /**
     * 用于清空输入框，并且往里输入东西
     *
     */
    public static void write(WebElement element, String s){
        element.clear();
        element.sendKeys(s);


    }

    /**
     * 用于设置让浏览器等待10秒后再关闭
     * @throws InterruptedException
     */
    public static void quit(WebDriver driver) {

        try {

            log4j.info("准备退出浏览器");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();

    }






}
