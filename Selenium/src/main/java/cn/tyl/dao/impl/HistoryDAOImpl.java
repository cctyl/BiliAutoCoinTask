package cn.tyl.dao.impl;

import cn.tyl.dao.HistoryDAO;
import cn.tyl.domain.HistoryVideo;
import cn.tyl.utils.BiliLogin;
import cn.tyl.utils.JDBCUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class HistoryDAOImpl implements HistoryDAO {

    public Logger log4j =  Logger.getLogger(BiliLogin.class);
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    public HistoryVideo getHistoryVideo() {

        log4j.info("getHistoryVideo 尝试从数据库中拿一条数据");

        String sql = "select * from HistoryVideo where coin_tag = 0 limit 1";


        HistoryVideo hvideo = template.queryForObject(sql,new BeanPropertyRowMapper<HistoryVideo>(HistoryVideo.class));

        log4j.info("getHistoryVideo拿到了一条数据，标题为："+hvideo.getVideo_title());

        return hvideo;

    }



    public int updateCoin_tag(HistoryVideo hvideo) {

        log4j.info("updateCoin_tag 尝试从数据库更改一条数据");
        String sql = "update favlist set coin_tag = 1 where video_link = ? ";

        int update = template.update(sql, hvideo.getVideo_link());

        log4j.info("updateCoin_tag更新了1条数据--"+hvideo.getVideo_link());
        return update;

    }

    public void saveVideoInfo(List<HistoryVideo> list) {

        String sql = "insert into HistoryVideo(view_time,video_partition,view_progress,video_title,video_link,video_author) values(?,?,?,?,?,?)";

        for (HistoryVideo hvideo : list) {

            template.update(sql,hvideo.getView_time(),hvideo.getVideo_partition(),hvideo.getView_progress(),hvideo.getVideo_title(),
                    hvideo.getVideo_link(),hvideo.getVideo_author()
                    );

        }

        log4j.info("saveVideoInfo存入了"+list.size()+"条数据");





    }


}
