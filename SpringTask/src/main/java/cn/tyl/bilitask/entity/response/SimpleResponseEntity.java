package cn.tyl.bilitask.entity.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponseEntity {

    private int code;
    private String message;
    private int ttl;
    private Object data;
}
