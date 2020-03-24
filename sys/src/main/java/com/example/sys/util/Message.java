package com.example.sys.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private String data;
    private Integer code;

    public Message(String data, Integer code) {
        this.data = data;
        this.code = code;
    }

}
