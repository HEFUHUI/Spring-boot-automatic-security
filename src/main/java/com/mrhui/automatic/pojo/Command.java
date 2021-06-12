package com.mrhui.automatic.pojo;


import lombok.Data;

@Data
public class Command {
    //命令类型
    private String cmdType;
    //命令数据
    private Object data;
    // 目的地
    private String destination;
}
