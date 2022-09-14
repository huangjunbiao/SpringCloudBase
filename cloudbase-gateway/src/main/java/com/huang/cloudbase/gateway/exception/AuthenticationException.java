package com.huang.cloudbase.gateway.exception;

/**
 * 认证失败 ，说明token有问题应当返回 401
 */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = -2212501482823023079L;

    private final int code;

    public AuthenticationException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
