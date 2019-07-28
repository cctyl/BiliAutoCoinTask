package cn.tyl.dao;

import cn.tyl.domain.Favlist;

import java.util.List;

/**
 * 操作Favlist对应表的dao
 */
public interface FavlistDAO {

    /**
     * 接受一个封装了数据的favlist对象，并把数据取出写入到数据库
     * @param favlist
     */
    public void saveVideoInfo(Favlist favlist);


    /**
     * 重载saveVideoInfo，变为批量存储favlist
     * @param list
     */
    public void saveVideoInfo(List<Favlist> list);

    /**
     * 查找数据库中所有的数据，并封装为javaBean返回
     * @return
     */
    public List<String> getAllVideoLink();


    /**
     * 用于把已投币视频的信息更新
     * @param favlist
     */
    public int updateCoin_tag(Favlist favlist);


    /**
     * 获取数据库中没有投过币的视频
     *
     * @return
     */
    public Favlist getVideo();
}
