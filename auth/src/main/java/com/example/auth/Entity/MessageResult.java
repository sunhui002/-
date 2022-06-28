package com.example.auth.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResult<T> {

    /**
     * @description: 统一数据返回结构
     * @author: huangkailong
     * @create: 2020-10-29
     **/
    private boolean Result;
    private String ErrorMessage;
    private T KeyValue;


    public static <T> MessageResult<T> success(T data){
        return new MessageResult<T>(data);
    }

    public static <T> MessageResult<T> success(){
        return new MessageResult<T>();
    }

    public static <T> MessageResult error(String _ErrorMessage){
        return new MessageResult<T>(_ErrorMessage);
    }


    public static <T> MessageResult Ok(boolean result){
        return new MessageResult<T>(result);
    }

    private MessageResult(T data){
        this.Result = true;
        this.ErrorMessage = "";
        this.KeyValue = data;
    }
    public MessageResult(){
        this.Result = true;
        this.ErrorMessage = "";
    }

    private MessageResult(String _ErrorMessage){
        this.Result = false;
        this.ErrorMessage = _ErrorMessage;
    }
    private MessageResult(boolean _Result){
        this.Result = _Result;
    }
    @JsonProperty("Result")
    public boolean getResult() {
        return Result;
    }

    public void setResult(Boolean Result) {
        this.Result = Result;
    }

    @JsonProperty("ErrorMessage")
    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String ErrorMessage) {
        this.ErrorMessage = ErrorMessage;
    }

    @JsonProperty("KeyValue")
    public T getKeyValue() {
        return KeyValue;
    }

    public void setKeyValue(T KeyValue) {
        this.KeyValue = KeyValue;
    }
}
