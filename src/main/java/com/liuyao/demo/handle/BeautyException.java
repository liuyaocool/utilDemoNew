package com.liuyao.demo.handle;

//可以在service 中抛出此异常
public class BeautyException extends RuntimeException{

    private Integer code;

    public BeautyException() {
    }

    public BeautyException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public BeautyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
