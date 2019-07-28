package cn.tyl.domain;

public class HistoryVideo {

    private int aid;//在表中的id

    public HistoryVideo(String view_time, String video_partition, String view_progress, String video_title, String video_link, String video_author) {
        this.view_time = view_time;
        this.video_partition = video_partition;
        this.view_progress = view_progress;
        this.video_title = video_title;
        this.video_link = video_link;
        this.video_author = video_author;
    }

    public HistoryVideo() {
    }

    @Override
    public String toString() {
        return "HistoryDAO{" +
                "aid=" + aid +
                ", view_time='" + view_time + '\'' +
                ", video_partition='" + video_partition + '\'' +
                ", view_progress='" + view_progress + '\'' +
                ", video_title='" + video_title + '\'' +
                ", video_link='" + video_link + '\'' +
                ", video_author='" + video_author + '\'' +
                ", coin_tag=" + coin_tag +
                '}';
    }




    private String view_time;//观看时间
    private String video_partition;//视频分区
    private String view_progress;//观看进度
    public String video_title;
    public String video_link;
    public String video_author;
    public int coin_tag;

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }


    public String getVideo_author() {
        return video_author;
    }

    public void setVideo_author(String video_author) {
        this.video_author = video_author;
    }



    public int getCoin_tag() {
        return coin_tag;
    }

    public void setCoin_tag(int coin_tag) {
        this.coin_tag = coin_tag;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getView_time() {
        return view_time;
    }

    public void setView_time(String view_time) {
        this.view_time = view_time;
    }

    public String getVideo_partition() {
        return video_partition;
    }

    public void setVideo_partition(String video_partition) {
        this.video_partition = video_partition;
    }

    public String getView_progress() {
        return view_progress;
    }

    public void setView_progress(String view_progress) {
        this.view_progress = view_progress;
    }
}
