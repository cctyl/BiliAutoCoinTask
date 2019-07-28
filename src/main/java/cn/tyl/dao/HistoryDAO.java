package cn.tyl.dao;

import cn.tyl.domain.Favlist;
import cn.tyl.domain.HistoryVideo;

import java.util.List;

/**
 * 用于操作HistoryVideo的dao类
 *
 */
public interface HistoryDAO {
    /**
     * 从数据库中拿一条数据用于投币
     * @return
     */
    public HistoryVideo getHistoryVideo();


    /**
     * 用于把已投币视频的信息更新
     * @param hvideo
     */
    public int updateCoin_tag(HistoryVideo hvideo);



    /**
     * 把传来的list中的数据写入数据库
     * @param list
     */
    public void saveVideoInfo(List<HistoryVideo> list);

}
