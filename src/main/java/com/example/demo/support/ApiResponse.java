package com.example.demo.support;

import java.io.Serializable;

public class ApiResponse<B> {

    private final B body;
    private final int status;

    public ApiResponse(final B body, final int status) {
        this.body = body;
        this.status = status;
    }

    public B getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    public static class FailureBody implements Serializable {
        private final String code;
        private final String message;

        public FailureBody(final String code, final String message) {
            this.code = code;
            this.message = message;
        }

        public FailureBody(final String message) {
            this.code = "ERROR";
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class SuccessBody<D> implements Serializable {
        private final D data;
        private final String message;
        private final String code;

        public SuccessBody(final D data, final String message, final String code) {
            this.data = data;
            this.message = message;
            this.code = code;
        }

        public D getData() {
            return data;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }
    }
}
