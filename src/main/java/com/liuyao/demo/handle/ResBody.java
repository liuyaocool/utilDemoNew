package com.liuyao.demo.handle;

import java.util.List;

public class ResBody<T> {

    /* 错误码 */
    private Integer code;

    /* 错误信息 */
    private String msg;

    /* 返回结果 */
    private T date;

    /* 返回结果集合 */
    private List<T> datas;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
