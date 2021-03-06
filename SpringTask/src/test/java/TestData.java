import cn.tyl.bilitask.BiliTaskApplication;

import cn.tyl.bilitask.entity.response.history.HistoryList;
import cn.tyl.bilitask.schedule.TaskSchedule;
import cn.tyl.bilitask.utils.BiliLiveUtils;
import cn.tyl.bilitask.utils.BiliVideoUtils;

import cn.tyl.bilitask.utils.MessageUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TestData {

    @Autowired
    BiliVideoUtils videoUtils;


    @Autowired
    TaskSchedule taskSchedule;

    @Autowired
    BiliLiveUtils biliLiveUtils;


    @Autowired
    MessageUtils messageUtils;
    /**
     * 测试投币是否成功
     */
    @Test
    public void test() {
       /* JsonNode liveGiftBagList = biliLiveUtils.getLiveGiftBagList();

        for (JsonNode jsonNode : liveGiftBagList) {
            String expire_at = jsonNode.get("expire_at").toPrettyString();
            System.out.println(expire_at);
        }*/


        messageUtils.sendMessage("第er次发送信息","111111111");
    }

}
