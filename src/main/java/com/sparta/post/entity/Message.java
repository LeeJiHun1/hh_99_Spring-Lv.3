package com.sparta.post.entity;

import lombok.Data;

@Data
public class Message {

    private int status;
    private String msg;

    public Message(){
        this.status = 200;
        this.msg = null;
    }
}
