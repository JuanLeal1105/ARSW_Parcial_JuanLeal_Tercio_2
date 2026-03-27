package edu.eci.arsw.blueprints.controllers.dto;

import org.springframework.http.HttpStatus;

public class ResponseDTO<T> {
    private int code;
    private HttpStatus httpStatus;
    private String status;
    private String message;
    private T data;

    public ResponseDTO(String status, String message, T data, int code, HttpStatus httpStatus) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public static <T> ResponseDTO<T> success(T data, String message, int code, HttpStatus httpStat) {
        return new ResponseDTO<>("Success", message, data, code, httpStat);
    }

    public static <T> ResponseDTO<T> error(String message, int code, HttpStatus httpStat) {
        return new ResponseDTO<>("Error", message, null, code, httpStat);
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
