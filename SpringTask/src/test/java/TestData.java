import cn.tyl.bilitask.BiliTaskApplication;
import cn.tyl.bilitask.entity.Data;

import cn.tyl.bilitask.schedule.TaskSchedule;

import cn.tyl.bilitask.task.impl.NewDailyTask;
import cn.tyl.bilitask.utils.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

//    @Autowired
//    private TaskSchedule taskSchedule;

    @Autowired
    RequestUtil requestUtil;

    @Autowired
    NewDailyTask newDailyTask;

    @Autowired
    ObjectMapper objectMapper;


/*
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


        System.out.println(newDailyTask.getCoin());
//        dailyTask.run();
    }
*/


    /**
     * 测试json的解析
     */
    @Test
    public void testJsonParse() throws JsonProcessingException {
        String get = "{\n" +
                "  \"code\": 0,\n" +
                "  \"status\": true,\n" +
                "  \"data\": {\n" +
                "    \"login\": true,\n" +
                "    \"watch_av\": true,\n" +
                "    \"coins_av\": 0,\n" +
                "    \"share_av\": false,\n" +
                "    \"email\": true,\n" +
                "    \"tel\": true,\n" +
                "    \"safequestion\": false,\n" +
                "    \"identify_card\": true,\n" +
                "    \"level_info\": {\n" +
                "      \"current_level\": 5,\n" +
                "      \"current_min\": 10800,\n" +
                "      \"current_exp\": 28275,\n" +
                "      \"next_exp\": 28800\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JsonNode jsonNode = objectMapper.readTree(get);
        JsonNode temp = jsonNode.get("data").get("coins_av");
        String s = temp.toPrettyString();

    }



}
