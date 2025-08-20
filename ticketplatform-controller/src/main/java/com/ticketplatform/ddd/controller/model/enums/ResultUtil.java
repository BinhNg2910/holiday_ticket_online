package com.ticketplatform.ddd.controller.model.enums;

import com.ticketplatform.ddd.controller.model.fe.ResultMessageModel;

/**
 * Service return result
 *
 * @Author BinhNguyen
 */
public class ResultUtil<T> {
    /**
     * Abstract class, store the result
     */
    private final ResultMessageModel<T> responseMessage;

    /**
     * Successful response
     */
    private static final Integer SUCCESS_CODE = 200;

    /**
     * Initialisation method, set default value for response value
     */
    public ResultUtil() {
        responseMessage = new ResultMessageModel<>();
        responseMessage.setSuccess(true);
        responseMessage.setMessage("success");
        responseMessage.setCode(SUCCESS_CODE);
    }

    /**
     * Return data type
     *
     * @param t data type
     * @return notification
     */
    public ResultMessageModel<T> setData(T t) {
        this.responseMessage.setResult(t);
        return this.responseMessage;
    }

    /**
     * Return successful notification
     *
     * @param resultCode return code
     * @return Return successful notification
     */
    public ResultMessageModel<T> setSuccessMsg(ResultCode resultCode) {
        this.responseMessage.setSuccess(true);
        this.responseMessage.setMessage(resultCode.getMessage());
        this.responseMessage.setCode(resultCode.getCode());
        return this.responseMessage;
    }

    /**
     * Static abstract method, return list of result
     *
     * @param t data type
     * @param <T> data type
     * @return Notification
     */
    public static <T> ResultMessageModel<T> data(T t) {
        return new ResultUtil<T>().setData(t);
    }

    /**
     * Return success
     *
     * @param responseStatusCode return code
     * @return Notification
     */
    public static <T> ResultMessageModel<T> success(ResultCode responseStatusCode) {
        return new ResultUtil<T>().setSuccessMsg(responseStatusCode);
    }

    /**
     * Return success
     *
     * @return notificatio
     */
    public static <T> ResultMessageModel<T> success() {
        return new ResultUtil<T>().setSuccessMsg(ResultCode.SUCCESS);
    }

    /**
     * Return fail
     *
     * @param code status code
     * @param msg notificaion return
     * @return notification
     */
    public static <T> ResultMessageModel<T> error(Integer code, String msg) {
        return new ResultUtil<T>().setErrorMsg(code, msg);
    }

    /**
     * Error server, add status code
     *
     * @param resultCode return code
     * @return notification
     */
    public ResultMessageModel<T> setErrorMsg(ResultCode resultCode) {
        this.responseMessage.setSuccess(false);
        this.responseMessage.setMessage(resultCode.getMessage());
        this.responseMessage.setCode(resultCode.getCode());
        return this.responseMessage;
    }

    /**
     * Error server, add status code
     *
     * @param code status code
     * @param msg return notification
     * @return notification
     */
    public ResultMessageModel<T> setErrorMsg(Integer code, String msg) {
        this.responseMessage.setSuccess(false);
        this.responseMessage.setMessage(msg);
        this.responseMessage.setCode(code);
        return this.responseMessage;
    }
}
