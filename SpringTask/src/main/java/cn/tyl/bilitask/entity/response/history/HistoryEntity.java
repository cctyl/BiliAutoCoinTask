package cn.tyl.bilitask.entity.response.history;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoryEntity {

    private long oid;
    private int epid;
    private String bvid;
    private int page;
    private long cid;
    private String part;
    private String business;
    private int dt;
}
