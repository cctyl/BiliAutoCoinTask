package cn.tyl.bilitask;

import cn.tyl.bilitask.schedule.TaskSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BiliTaskApplication {



    public static void main(String[] args) {
        SpringApplication.run(BiliTaskApplication.class,args);


    }
}
