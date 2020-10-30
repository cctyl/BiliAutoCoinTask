package cn.tyl.dao.impl;

import cn.tyl.dao.FavlistDAO;
import cn.tyl.domain.Favlist;
import cn.tyl.utils.BiliLogin;
import cn.tyl.utils.JDBCUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class FavlistDAOImpl implements FavlistDAO {

    public  Logger log4j =  Logger.getLogger(BiliLogin.class);
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    public void saveVideoInfo(Favlist favlist) {

        String sql = "insert into favlist(video_title,video_link,video_author,video_pubdate,fav_time) values(?,?,?,?,?)";

//        log4j.info(favlist);
        template.update(sql,favlist.getVideo_title(),favlist.getVideo_link(),favlist.getVideo_pubdate(),favlist.getVideo_author(),favlist.getFav_time());

        log4j.info("saveVideoInfo存入了1条数据"+"--"+favlist.getVideo_link());

    }

    public void saveVideoInfo(List<Favlist> list) {
        String sql = "insert into favlist(video_title,video_link,video_pubdate,video_author,fav_time) values(?,?,?,?,?)";

        for (Favlist favlist : list) {

            template.update(sql,favlist.getVideo_title(),favlist.getVideo_link(),favlist.getVideo_pubdate(),favlist.getVideo_author(),favlist.getFav_time());

        }

        log4j.info("saveVideoInfo存入了"+list.size()+"条数据");



    }

    public List<String> getAllVideoLink() {

        String sql = "select video_link from favlist";

        //查询许多数据并且封装为javaBean
        List<String> videoLinks = template.queryForList(sql,String.class);

        log4j.info("getAllVideoLink拿到了"+videoLinks.size()+"条数据");

        return videoLinks;
    }

    public int updateCoin_tag(Favlist favlist) {

        String sql = "update favlist set coin_tag = 1,video_type = ? where video_link = ? ";

        int update = template.update(sql, favlist.getVideo_type(),favlist.getVideo_link());

        log4j.info("updateCoin_tag更新了1条数据--"+favlist.getVideo_link());
        return update;

    }

    public Favlist getVideo() {

        String sql = "select * from favlist where coin_tag = 0 limit 1";

        Favlist favlist = template.queryForObject(sql, new BeanPropertyRowMapper<Favlist>(Favlist.class));

        log4j.info("getVideo拿到了一个视频--"+favlist.getVideo_link());

        return favlist;


    }
}
