package com.example.demo.support;

public class ApiResponseGenerator {

    public static ApiResponse<ApiResponse.SuccessBody<Void>> success(final int status) {
        return new ApiResponse<>(
                new ApiResponse.SuccessBody<>(null, "Success", "SUCCESS_CODE"),status);
    }

    public static ApiResponse<ApiResponse.SuccessBody<Void>> success(
            final int status, final String message, final String code) {
        return new ApiResponse<>(
                new ApiResponse.SuccessBody<>(null, message, code),
                status
        );
    }

    public static <D> ApiResponse<ApiResponse.SuccessBody<D>> success(
            final D data, final int status) {
        return new ApiResponse<>(
                new ApiResponse.SuccessBody<>(
                        data, "Success", "SUCCESS_CODE"),
                status
        );
    }

    public static <D> ApiResponse<ApiResponse.SuccessBody<D>> success(
            final D data, final int status, final String message, final String code) {
        return new ApiResponse<>(
                new ApiResponse.SuccessBody<>(data, message, code),
                status
        );
    }

    public static ApiResponse<ApiResponse.FailureBody> fail(
            final String code, final String message, final int status) {
        return new ApiResponse<>(
                new ApiResponse.FailureBody(code, message),
                status
        );
    }

    public static ApiResponse<ApiResponse.FailureBody> fail(
            final String message, final int status) {
        return new ApiResponse<>(
                new ApiResponse.FailureBody(message),
                status
        );
    }
}
