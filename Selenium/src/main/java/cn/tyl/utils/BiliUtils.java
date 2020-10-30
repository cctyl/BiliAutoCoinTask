package cn.tyl.utils;

import cn.tyl.dao.FavlistDAO;
import cn.tyl.dao.impl.FavlistDAOImpl;
import cn.tyl.domain.Favlist;
import cn.tyl.domain.HistoryVideo;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BiliUtils {

    public static Logger log4j = Logger.getLogger(BiliUtils.class);

    /**
     * 投币功能
     * 接收一个链接，把链接中的视频进行投币
     * 投币成功返回true
     * 投币不成功返回false（方便统计投币数量）
     *
     * @param driver
     * @return
     * @throws InterruptedException
     */
    public static boolean biliCoin(WebDriver driver, Favlist favlist) throws InterruptedException {


        String url = favlist.getVideo_link();
        //打开投币页面
        driver.get(url);
        boolean coin_tag = false;

        if (url.contains("bangumi")) {
            log4j.info("匹配到番剧类型的视频，调用番剧的投币方法");
            coin_tag = coinForBangumi(driver, url);

            //写入类型
            favlist.setVideo_type("bangumi");

        } else {
            log4j.info("没有匹配到，调用普通的投币方法");
            coin_tag = coinForOrdinary(driver, url);

            //不用更改类型，就是默认的类型
        }




        //返回true表明本次投币成功，返回false表明此视频已投过币，不能再投，应投下一个视频
        return coin_tag;
    }

    /**
     *
     * 重构方法，用于historyvideo的投币
     * 投币功能
     * 接收一个链接，把链接中的视频进行投币
     * 投币成功返回true
     * 投币不成功返回false（方便统计投币数量）
     *
     * @param driver
     * @return
     * @throws InterruptedException
     */
    public static boolean biliCoin(WebDriver driver, HistoryVideo historyVideo) throws InterruptedException {


        String url = historyVideo.getVideo_link();
        //打开投币页面
        driver.get(url);
        boolean coin_tag = false;

        if (url.contains("bangumi")) {
            log4j.info("匹配到番剧类型的视频，调用番剧的投币方法");
            coin_tag = coinForBangumi(driver, url);



        } else {
            log4j.info("没有匹配到，调用普通的投币方法");
            coin_tag = coinForOrdinary(driver, url);


        }




        //返回true表明本次投币成功，返回false表明此视频已投过币，不能再投，应投下一个视频
        return coin_tag;
    }


    public static boolean isCoinForOrdinary(WebDriver driver) {
        //  xpath表达式  //span[@class='coin on']

        try {
            Thread.sleep(5000);//等待加载
            WebElement coinClick = driver.findElement(By.xpath("//span[@class='coin on']"));
        } catch (Exception e) {


            log4j.info("没有coin on这个标签，也许没投过币");
            return true;

        }


        /**
         * 有coin说明是自制作品，但还不能判断能不能投币
         * 但是有coin on一定代表已经投过币
         * 则通过coin on的异常来进行第一步判断是否能投币
         *
         */
        return false;//有coin on一定代表已经投过币
    }


    public static boolean isCoinForBangumi(WebDriver driver) {

        try {
            Thread.sleep(2000);//等待加载
            WebElement coinClick = driver.findElement(By.xpath("//*[@id='toolbar_module']/div[@class='coin-info active']"));
            return false;
        } catch (Exception e) {


            log4j.info("这个标签没有active，也许没投过币");
            return true;

        }


    }


    public static boolean coinForBangumi(WebDriver driver, String url) throws InterruptedException {


        // //*[@id="toolbar_module"]/div[1]
        //  投币是否成功 默认投币失败
        boolean coin_tag = false;

        if (isCoinForBangumi(driver)) {
            //以下为投币操作
            //点击投币
            Thread.sleep(3000);
            WebElement coin = driver.findElement(By.xpath("//*[@id='toolbar_module']/div[1]"));
            coin.click();

            //点击确定按钮
            Thread.sleep(1000);
            //是否投过币
            boolean coinOrN = false;
            //多点几次，尝试抓取错误提示框
            //第一次似乎都是抓不到的

            for (int i = 0; i < 5; i++) {
                try {
                    WebElement bi_btn = driver.findElement(By.xpath("//*[@id='app']/div[5]/div/div[3]/span"));
                    ((JavascriptExecutor) driver).executeScript("$(arguments[0]).click()", bi_btn);

                    driver.findElement(By.xpath("/html/body/div[4]/div[1]"));
                    coinOrN = true;
                    log4j.info("抓到了，说明已经投过币了，不能再投币了");//能执行到这一步说明存在那个元素，即出现了提示框


                } catch (Exception e) {
                    //抓很多次，可能有些没抓到，就要报错提示，不希望看到这样
                    //没投过币才能报投币成功

                    log4j.info("第" + i + 1 + "次没抓到");
                    if (i == 4 && coinOrN == false) {//抓了5次还是没有提示，说明真的没投过币
                        log4j.info("投币成功");
                        coin_tag = true;
                    }
                }
            }


        } else {
            //直接抓取到coin-on标签，说明就是投过币的视频无需继续判断
            log4j.info("此视频已经投过币");

        }


        return coin_tag;


    }


    /**
     * 由于bilibili对不同种类视频有不同的页面，单独一个coin方法已经无法满足
     * 因此拆分为面向普通视频的coin方法以及面向其他视频的coin方法
     *
     * @param driver
     * @param url
     * @return
     */
    public static boolean coinForOrdinary(WebDriver driver, String url) throws InterruptedException {

        //  投币是否成功 默认投币失败
        boolean coin_tag = false;

        if (isCoinForOrdinary(driver)) {
            //以下为投币操作
            //点击投币
            Thread.sleep(3000);
            WebElement coin = driver.findElement(By.xpath("//span[@class='coin']"));
            coin.click();

            //点击确定按钮
            Thread.sleep(1000);
            //是否投过币
            boolean coinOrN = false;
            //多点几次，尝试抓取错误提示框
            //第一次似乎都是抓不到的

            for (int i = 0; i < 5; i++) {
                try {
                    WebElement bi_btn = driver.findElement(By.xpath("//span[@class='bi-btn']"));
                    ((JavascriptExecutor) driver).executeScript("$(arguments[0]).click()", bi_btn);

                    driver.findElement(By.xpath("//div[@class='bili-msg show error']"));
                    coinOrN = true;
                    log4j.info("抓到了，说明已经投过币了，不能再投币了");//能执行到这一步说明存在那个元素，即出现了提示框


                } catch (Exception e) {
                    //抓很多次，可能有些没抓到，就要报错提示，不希望看到这样
                    //没投过币才能报投币成功

                    log4j.info("第" + (i + 1) + "次没抓到");
                    if (i == 4 && coinOrN == false) {//抓了5次还是没有提示，说明真的没投过币
                        log4j.info("投币成功");
                        coin_tag = true;
                    }
                }
            }


        } else {
            //直接抓取到coin-on标签，说明就是投过币的视频无需继续判断
            log4j.info("此视频已经投过币");

        }


        return coin_tag;
    }


    /**
     * 获取收藏夹中视频信息，用于更新数据库中数据
     *
     * @param driver
     * @return
     */
    public static List<Favlist> getFavlist(WebDriver driver) {

        log4j.info("开始从收藏夹中收集信息 -- getFavlist");

        ArrayList<Favlist> listAll = null;
        try {
            //进入收藏页面
            driver.get("https://space.bilibili.com/8427106/favlist");


            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            listAll = new ArrayList<Favlist>();

            //获取收藏夹总页数

            String lastPage = driver.findElement(By.xpath("//*[@id='page-fav']/div[1]/div[2]/div[3]/ul[2]/li[6]")).getAttribute("title");


            int totalPage = Integer.parseInt(lastPage.replace("最后一页:", ""));

            log4j.info("获取收藏夹总页数：" + totalPage);
            for (int i = 1; i < totalPage; i++) {
                log4j.info("正在获取第" + i + "页的视频");

                listAll.addAll(collectVideo(driver));

                //点击下一页
                WebElement nextPage = driver.findElement(By.xpath("//*[@id='page-fav']/div[1]/div[2]/div[3]/ul[2]/li[@class='be-pager-next']"));
                ((JavascriptExecutor) driver).executeScript("$(arguments[0]).click()", nextPage);
                log4j.info("点击了一次下一页");
                //等待页面加载
                Thread.sleep(3000);

            }
            log4j.info("正在获取第最后一页的视频");
            listAll.addAll(collectVideo(driver));//本次加上循环次数，刚好10次，满足页面数

            log4j.info("收藏夹总共有" + listAll.size() + "个视频");


        } catch (InterruptedException e) {
            log4j.error("出错了，错误为-----" + e.getMessage());
        }

        return listAll;
    }


    public static List<Favlist> collectVideo(WebDriver driver) {

        List<Favlist> flList = null;
        try {
            //只获取未失效的视频
            //获取了当前页的视频
            log4j.info("调用了collectVideo方法");
            List<WebElement> lis = driver.findElements(By.xpath("//li[@class='small-item']"));
            log4j.info("当前页面共有" + lis.size() + "个有效视频");

            flList = new ArrayList<Favlist>();


            if (lis != null && lis.size() > 0)
                for (WebElement li : lis) {
                    Thread.sleep(1000);
                    WebElement label_a = li.findElement(By.xpath("./a[@class='title']"));//a标签

                    Thread.sleep(2000);

                    log4j.info("获取每个视频的详细信息");
                    String video_link = label_a.getAttribute("href");
                    String video_title = label_a.getAttribute("title");
                    String fav_time = li.findElement(By.xpath("./div[@class='meta pubdate']")).getText().replace("收藏于： ", "");
                    log4j.info("鼠标悬浮在视频封面上");

                    //似乎是没有显示屏，鼠标即使悬浮，也获取不到数据
                    Actions action = new Actions(driver);
                    WebElement touch = li.findElement(By.xpath("./a[1]"));
                    action.clickAndHold(touch).perform();
                    action.clickAndHold(touch).perform();

                    String video_author = li.findElement(By.xpath("./a[1]/div[1]/div/p[3]")).getText().replace("UP主：", "");
                    String video_pubdate = li.findElement(By.xpath("./a[1]/div[1]/div/p[4]")).getText().replace("投稿：", "");


                    Favlist fv = new Favlist(video_title, video_link, video_pubdate, video_author, fav_time);
                    flList.add(fv);


                    log4j.info("list中添加了一个视频，视频标题是--" + video_title + "--作者--" + video_author + "--收藏时间--" + video_pubdate);


                }
            else
                log4j.info("lis未获取成功");


        } catch (InterruptedException e) {
            log4j.error("出错了，错误为-----" + e.getMessage());
        }
        return flList;

    }


    /**
     * 更新数据库中的视频，返回更新的记录数
     *
     * @param driver
     */
    public static int updateFavlistVideo(WebDriver driver) {

        //从浏览器中拿数据
        //先登录
        login(driver);

        //从收藏夹中拿视频
        List<Favlist> favLists = getFavlist(driver);

        //从数据库中拿数据
        FavlistDAO dao = new FavlistDAOImpl();
        log4j.info("尝试从数据库中拿出了已有视频的链接");
        List<String> videoLinks = dao.getAllVideoLink();
        if (videoLinks.size() > 0 && videoLinks != null)
            for (String videoLink : videoLinks) {
                log4j.info("从数据库中拿出来的链接是" + videoLink);
            }
        else
            log4j.info("从数据库读取数据失败");


        //进行去重处理
        //因在遍历的同时操作了集合，所以报错，采用iterater来循环解决
        Iterator<Favlist> iterator = favLists.iterator();
        while (iterator.hasNext()) {

            Favlist video = iterator.next();

            boolean containsOrN = videoLinks.contains(video.getVideo_link());
            if (containsOrN) {
                //包含，即重复了，把这个video从favLists中删除
                iterator.remove();
                log4j.info("重复了，重复的视频是" + video.getVideo_title() + "--尝试删除他 ");

            }
//                //不包含，即没有重复
//                //让这个video继续保留,即什么也不做

        }
        int updateNum = favLists.size();

        //处理后的favLists就是不包含重复的
        //把处理后的数据写入数据库
        log4j.info("本次找到的新视频共有" + updateNum + "条，准备写入数据库");
        dao.saveVideoInfo(favLists);

        return updateNum;


    }


    /**
     * 用于判断是否登录bilibili
     * 原理是：如果已经登录bilibili，再打开https://passport.bilibili.com/login 会自动跳转到 https://www.bilibili.com/
     * 只要打开先打开登录页面，稍等3秒，若页面链接变成首页，则判断为登录成功
     * driver.getCurrentUrl()获取当前链接
     *
     * @param driver
     * @return
     */

    //因为需要调用自己，所以留下个变量控制重试次数
    public static int reCount = 0;

    public static void login(WebDriver driver) {

        log4j.info("准备进行登录操作");
        driver.get("https://passport.bilibili.com/login");
        String currentUrl = "";
        try {
            Thread.sleep(3000);
            currentUrl = driver.getCurrentUrl();


        } catch (InterruptedException e) {
            log4j.error("出错了，错误为-----" + e.getMessage());
        }

        boolean result = "https://www.bilibili.com/".equals(currentUrl);

        if (!result) {
            //未登录
            try {
                log4j.info("未登录，调用 BiliLogin.login进行登录");
                BiliLogin.login(driver);
                Thread.sleep(5000);
                if (reCount < 10) {
                    reCount++;
                    log4j.info("重试登录，第" + reCount + "次重试");
                    BiliUtils.login(driver);


                } else {
                    log4j.info("重试次数已经达到" + reCount + "次，仍然未登录成功");
                    throw new RuntimeException("登录失败");
                }


            } catch (InterruptedException e) {
                log4j.error("登录失败，错误为-----" + e.getMessage());
            }

        } else {
            log4j.info("已登录");
        }
    }


    /**
     * 用于收集昨天观看过的视频
     * 然后把视频信息封装到javaBean里面返回一个list集合
     *
     * @param driver
     * @return
     */
    public static ArrayList<HistoryVideo> collectHistoryVideo(WebDriver driver) {


        // 因为此方法独立于其他方法，只是完成单一的任务，为方便，登录操作在此方法内部完成
        login(driver);
        //用于存储javaBean的集合
        ArrayList<HistoryVideo> beanList = null;

        try {
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
            beanList = new ArrayList<HistoryVideo>();

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
        } catch (InterruptedException e) {
            log4j.error(e.getMessage());
        }

        log4j.info("数据收集完毕");

        return beanList;

    }


}
