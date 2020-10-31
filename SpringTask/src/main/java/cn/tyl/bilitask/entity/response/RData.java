package cn.tyl.bilitask.entity.response;

import cn.tyl.bilitask.entity.response.history.HistoryList;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class RData{


    private List<HistoryList> list;
}
