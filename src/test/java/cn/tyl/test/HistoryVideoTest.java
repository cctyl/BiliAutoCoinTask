package cn.tyl.test;

import cn.tyl.domain.HistoryVideo;
import cn.tyl.selenium.BiliTimer;
import cn.tyl.utils.BiliUtils;
import cn.tyl.utils.SeleniumUtil;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HistoryVideoTest {

    /*

    获取页面上30条数据，分析出昨天的数据，再开始对数据拆分存入数据库


     */
    public static Logger log4j = Logger.getLogger(HistoryVideoTest.class);

    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            ChromeOptions chromeOptions = new ChromeOptions();

            chromeOptions.addArguments("--user-data-dir=/home/bim/.config/google-chrome/Default2");

            driver = new ChromeDriver(chromeOptions);


            //登录
//        BiliUtils.login(driver);


            //打开历史记录页面
            log4j.info("打开历史记录页面");
            driver.get("https://www.bilibili.com/account/history");

            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);


            log4j.info("获取50条list数据");
            //获取50条list数据
            List<WebElement> elements = driver.findElements(By.xpath("//ul[@id='history_list']/li"));

            //存储昨天看过的视频列表
            ArrayList<WebElement> yesterday = new ArrayList<WebElement>();

            int i = 0;
            boolean time_tag = false; //true表示这个视频是昨天的，可以添加，false表示是其他时间的
            log4j.debug("开始循环");
            for (WebElement element : elements) {

                String className = element.getAttribute("class");

                log4j.info(" 获取到的classname是--" + className);
                if (!className.equals("history-record")) {
                    //不包含昨天或今天的视频，即标签仅仅为history-record，就是普通视频，不用判断了

                    if (className.contains("lastdayitem")) {
                        //包含lastdayitem，说明已经遍历完今天的内容，开始遍历昨天的内容
                        //让time_tag改为true，意味着这个视频可以添加
                        log4j.info("这是昨天的");
                        time_tag = true;

                    } else if (className.contains("lastweekitem")) {
                        //匹配到lastweekitem说明昨天的视频已经遍历完，可以停下了。让time_tag改为false
                        time_tag = false;
                        log4j.info("这不！！是昨天的");
                    }


                }


                //根据time_tag来决定这个视频是否添加到yesterday list中
                if (time_tag) {

                    log4j.info("添加了一条昨天的视频到预备集合中");
                    yesterday.add(element);
                }


                //控制循环次数
                i++;
                if (i > 50) {
                    break;


                }


            }

            //创建今日时间以供反复使用

            Date today = new Date();
            long oneDay = 24 * 3600 * 1000; //一天的毫秒值
            Date yesterdayTime = new Date(today.getTime() - oneDay);
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String formatResult = format.format(yesterdayTime);
            log4j.info("昨天的日期是：" + formatResult);


            //创建一个list用于存储封装好数据的javaBean
            ArrayList<HistoryVideo> beanList = new ArrayList<HistoryVideo>();

            //经过上面那个循环的处理，yestday中的视频应该都是昨天的
            for (WebElement webElement : yesterday) {
                //获取标题 //ul[@id='history_list']/li/div[2]/div[2]/a
                //    ./div[2]/div[2]/a
                Thread.sleep(1000);

                // 获取视频标题
                String video_title = webElement.findElement(By.xpath("./div[2]/div[2]/a")).getText();

                //  获取观看进度 //*[@id="history_list"]/li[2]/div[2]/div[2]/div/div/span
                String view_progress = webElement.findElement(By.xpath("./div[2]/div[2]/div/div/span")).getText();

                Thread.sleep(500);
                //获取视频作者
                String video_author = null;
                try {
                    video_author = webElement.findElement(By.xpath("./div[2]/div[2]/div/span/a/span")).getText();
                } catch (NoSuchElementException e) {
                    log4j.error(e.getMessage());
                    video_author = "bilibili番剧";
                }
                Thread.sleep(500);

                //获取视频分区
                String video_partition = null;
                try {
                    video_partition = webElement.findElement(By.xpath("./div[2]/div[2]/div/span/span")).getText();
                } catch (NoSuchElementException e) {
                    log4j.error(e.getMessage());
                    video_partition = "番剧";
                }

                Thread.sleep(500);
                //获取视频观看时间，因为都是昨天的，所以时间全部减去24小时
                String view_time = formatResult + " - " + webElement.findElement(By.xpath("./div[1]/div/span")).getText();

                Thread.sleep(500);
                //先声明视频链接
                String video_link = null;
                //获取当前窗口句柄
                String nowWindows = driver.getWindowHandle();
                //获取视频链接
                //点击视频标题，进入视频详情页获取视频链接
                log4j.info("点击了一次标题：" + video_title);
                webElement.findElement(By.xpath("./div[2]/div[2]/a")).click();

                //获取所有窗口句柄
                Set<String> windowHandles = driver.getWindowHandles();
                for (String windowHandle : windowHandles) {
                    if (windowHandle.equals(nowWindows) == false) {
                        //不是原窗口，是新窗口
                        driver.switchTo().window(windowHandle);

                        Thread.sleep(1500);
                        video_link = driver.getCurrentUrl();

                        //关闭当前窗口
                        driver.close();

                        //切换回原窗口
                        driver.switchTo().window(nowWindows);

                    }
                }



                Thread.sleep(1000);


                //把以上数据封装到javaBean中
                HistoryVideo hVideo = new HistoryVideo(view_time, video_partition, view_progress, video_title, video_link, video_author);

                log4j.info(" 添加了一个bean到beanList中");
                beanList.add(hVideo);


                log4j.debug(hVideo.toString());



            }

            log4j.info("数据收集完毕");



        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            SeleniumUtil.quit(driver);
        }


    }


}
