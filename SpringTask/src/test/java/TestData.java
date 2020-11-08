import cn.tyl.bilitask.BiliTaskApplication;

import cn.tyl.bilitask.schedule.TaskSchedule;
import cn.tyl.bilitask.utils.BiliVideoUtils;

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
    BiliVideoUtils videoUtils;


    @Autowired
    TaskSchedule taskSchedule;


    /**
     * 测试投币是否成功
     */
    @Test
    public void testThrowCoin() {

        taskSchedule.throwCoinTask();
        taskSchedule.viewAndShareTask();

    }

}
