import cn.tyl.bilitask.BiliTaskApplication;
import cn.tyl.bilitask.entity.Data;
import cn.tyl.bilitask.schedule.TaskSchedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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



    @Test
    public void testAutowired() {

        System.out.println(data.toString());
    }


    @Test
    public  void testCheck(){

        System.out.println(taskSchedule.check());
    }
}
