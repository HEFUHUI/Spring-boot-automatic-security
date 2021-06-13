package com.mrhui.automatic.pojo;

import lombok.Data;

@Data
public class WebsocketReceive {
    private int code;
    private Object data;
    private int isPage = 0;
    private Paging paging = new Paging();
}


@Data
class Paging{
    private int total = 0;
    private int current = 0;
}