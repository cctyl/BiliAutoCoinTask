import cn.tyl.bilitask.BiliTaskApplication;
import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.schedule.TaskSchedule;
import cn.tyl.bilitask.task.DailyTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * 测试类，测试自动从配置文件读取数据是否成功
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BiliTaskApplication.class})
public class TestData {

    @Autowired
    private Data data;

    @Autowired
    private TaskSchedule taskSchedule;


    @Autowired
    DailyTask dailyTask;


    @Test
    public void testAutowired() {

        System.out.println(data.toString());
    }


    @Test
    public  void testCheck(){

        System.out.println(taskSchedule.check());
    }


    @Test
    public void testDailytask(){
        List<HistoryList> history = dailyTask.getHistory(10);
        history.forEach(System.out::println);

    }



}
