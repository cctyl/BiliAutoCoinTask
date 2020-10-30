package cn.tyl.domain;

/**
 * 用于对应bilibili下的favlist表
 */
public class Favlist {

    private int aid;

    private String fav_time;
    public String video_title;
    public String video_link;
    public String video_pubdate;
    public String video_author;
    public String video_type;
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

    public String getVideo_pubdate() {
        return video_pubdate;
    }

    public void setVideo_pubdate(String video_pubdate) {
        this.video_pubdate = video_pubdate;
    }

    public String getVideo_author() {
        return video_author;
    }

    public void setVideo_author(String video_author) {
        this.video_author = video_author;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public int getCoin_tag() {
        return coin_tag;
    }

    public void setCoin_tag(int coin_tag) {
        this.coin_tag = coin_tag;
    }

    @Override
    public String toString() {
        return "Favlist{" +
                "aid=" + aid +
                ", video_title='" + video_title + '\'' +
                ", video_link='" + video_link + '\'' +
                ", video_pubdate='" + video_pubdate + '\'' +
                ", video_author='" + video_author + '\'' +
                ", fav_time='" + fav_time + '\'' +
                ", video_type='" + video_type + '\'' +
                ", coin_tag=" + coin_tag +
                '}';
    }


    public Favlist() {
    }

    public Favlist(String video_title, String video_link, String video_pubdate, String video_author, String fav_time) {
        this.video_title = video_title;
        this.video_link = video_link;
        this.video_pubdate = video_pubdate;
        this.video_author = video_author;
        this.fav_time = fav_time;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getFav_time() {
        return fav_time;
    }

    public void setFav_time(String fav_time) {
        this.fav_time = fav_time;
    }
}
