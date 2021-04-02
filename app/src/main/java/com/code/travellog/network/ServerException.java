package com.code.travellog.network;

/**
 * @description Server Exception
 * @time 2021/1/13 8:41
 */

public class ServerException extends RuntimeException {

    public String message;

    public int code;

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
