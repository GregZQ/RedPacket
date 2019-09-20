package com.boot.domain.result;

import com.boot.meta.ResultCodeMeta;

import java.io.Serializable;


public class Result<T> implements Serializable {
    private int status;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result(int status , T data){
        this.status =status;
        this.data =data;
    }


    public static Result isOk(){
        return Result.build(ResultCodeMeta.SUCCESS,null);
    }

    public static <T>Result isOk(T t){
        return Result.build(ResultCodeMeta.SUCCESS, t);
    }

    public static <T>Result build(int code, T message){
        return new Result(code,message);
    }

}
