package com.example.logsys.util;

import lombok.Data;
import java.io.Serializable;

@Data
public class MessageVO implements Serializable {

    private String data;

    private Integer code;

    public MessageVO(String data, Integer code) {
        this.data = data;
        this.code = code;
    }

}
