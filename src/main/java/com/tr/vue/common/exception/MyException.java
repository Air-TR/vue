package com.tr.vue.common.exception;


import com.tr.vue.common.result.ResultEnum;

/**
 * 自定义异常
 *
 * @author taorun
 * @date 2017年11月21日 下午3:24:53
 */
public class MyException extends RuntimeException {

    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public MyException(ResultEnum resultEnum, Exception exception) {
        super(resultEnum.getMsg() + " : " + exception.getMessage());
        this.code = resultEnum.getCode();
    }

    public MyException(String string) {
        super(string);
        this.code = ResultEnum.FAIL.getCode();
    }

    public MyException(Integer code, String string) {
        super(string);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
