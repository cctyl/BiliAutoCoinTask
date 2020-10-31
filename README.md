# BiliAutoCoinTask



Selenium方式（旧项目）：

完成bilibili每日自动登录以及自动投币任务

利用Selenium完成每日自动登录（模拟滑块验证）
工作原理：
分支1：爬取bilibili收藏夹中的视频，存入数据库。每天定时从数据库中取出视频投币，并记录下投币结果。第二天再从数据库中取出未投币的视频继续投币。
分支2：每日定时爬取昨天观看过的视频并存入数据库。当收藏夹中没有未投币视频，则从历史记录的数据库中拿取视频继续投币，投币结果同样写回数据库中。

目前每日任务依靠的是linux的crontab来完成，调用的是BiliTimer2



SpringTask:

​	对原项目https://github.com/srcrs/BilibiliTask 的改写。

* 大体框架使用SpringBoot
* 修改了对配置文件的读写
* 修改了数据的获取来源
* 修改定时任务的执行方式