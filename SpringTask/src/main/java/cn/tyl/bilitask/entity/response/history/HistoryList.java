package cn.tyl.bilitask.entity.response.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryList {

    private String title;
    private String long_title;
    private String cover;
    private String covers;
    private String uri;
    private HistoryEntity history;
    private int videos;
    private String author_name;
    private String author_face;
    private long author_mid;
    private long view_at;
    private int progress;
    private String badge;
    private String show_title;
    private int duration;
    private String current;
    private int total;
    private String new_desc;
    private int is_finish;
    private int is_fav;
    private long kid;
    private String tag_name;
    private int live_status;
}
