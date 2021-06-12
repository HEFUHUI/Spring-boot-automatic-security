package com.mrhui.automatic.pojo;

import com.mrhui.automatic.entity.TUser;
import lombok.Data;
import lombok.ToString;
import lombok.val;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;


@Data
@ToString
public class StandardResult<T> implements Serializable {
    private static final long serialVersionUID = 2L;

    private String msg;//描述消息
    private int code;//返回代码 0表示失败 200表示成功
    private T data;// 数据
    private TUser author;// 数据


    public static<T> StandardResult<T>
    success(String msg, int code, T data, TUser author){
        final StandardResult<T> tStandardResult = new StandardResult<>();
        tStandardResult.author = author;
        tStandardResult.code = code;
        tStandardResult.data = data;
        tStandardResult.msg = msg;
        return tStandardResult;
    }

    public static<T> StandardResult<T> success(){
        return success("操作成功!",HttpStatus.OK.value(),null,null);
    }

    public static<T> StandardResult<T> success(String msg){
        return success(msg,HttpStatus.OK.value(),null,null);
    }

    public static<T> StandardResult<T> success(String msg,int code){
        return success(msg, code,null,null);
    }

    public static<T> StandardResult<T> success(String msg,T data){
        return success(msg,HttpStatus.OK.value(),data);
    }

    public static<T> StandardResult<T> success(int code){
        return success("操作成功！",code);
    }

    public static<T> StandardResult<T> success(T data){
        return success("操作成功！",data);
    }

    public static<T> StandardResult<T> success(String msg,int code,T data){
        return success(msg, code, data,null);
    }

    public static<T> StandardResult<T> success(TUser author){
        StandardResult<T> success = success();
        success.author = author;
        return success;
    }

    public static<T> StandardResult<T> failed(String msg){
        StandardResult<T> standardResult =  new StandardResult<T>();
        standardResult.msg = msg;
        return standardResult;
    }
    public static<T> StandardResult<T> failed(int code){
        return failed("操作失败",code);
    }
    public static<T> StandardResult<T> failed(){
        return failed(HttpStatus.NOT_ACCEPTABLE.value());
    }
    public static<T> StandardResult<T> failed(String msg, int code){
        StandardResult<T> standardResult =  failed(msg);
        standardResult.code = code;
        return standardResult;
    }
}
