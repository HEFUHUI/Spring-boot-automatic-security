package com.mrhui.automatic.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    //命令类型
    private String action;
    //命令数据
    private Object data;
    public static Command handle(Object data){
        return new Command("handle",data);
    }

    public static Command move(Object data){
        return new Command("move",data);
    }

    public static Command radio(Object data){
        return new Command("radio",data);
    }
}
