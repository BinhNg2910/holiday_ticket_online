package com.ticketplatform.ddd.controller.model.fe;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultMessageModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * sucessfull flag
     */
    private boolean success;

    /**
     * notification
     */
    private String message;

    /**
     * return code from frontend
     */
    private Integer code;

    /**
     * time
     */
    private long timestamp = System.currentTimeMillis();
    /**
     * result object
     */
    private T result;
}
