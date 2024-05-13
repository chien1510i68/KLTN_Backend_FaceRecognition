package com.example.backend_facerecognition.constant;

public class ErrorCodeDefs {
    public static final int ERR_OK = 0;
    public static final int ERR_SYSTEM = ERR_OK + 1;
    public static final int ERR_VALIDATION = ERR_OK + 2;
    public static final int ERR_TOKEN_INVALID = ERR_OK + 3;
    public static final int ERR_INTERNAL_SERVICE = ERR_OK + 4;
    public static final int ERR_UNAUTHORIZED = ERR_OK + 5;
    public static final int ERR_BAD_REQUEST = 400;
    public static final int ERR_PERMISSION_INVALID = 401;
    public static final int ERR_REQUEST_NOT_FOUND = 404;
    public static final int ERR_METHOD_NOT_ALLOWED = 405;
    public static final int ERR_SERVER_ERROR = 500;
    public static final int ERR_GATEWAY_ERROR = 502;
    public static final int ERR_HEADER_TOKEN_REQUIRED = 98;
    public static final int TOKEN_INVALID = 501;
    public static final int UNAUTHORIZED_TOKEN = 503;
    public static final int ACCESS_DENIED = 504;
    public static final int USER_EXISTED = 505;
    public static final int FAILED_PASSWORD = 402;


    public static String getMessage(int errorCode) {
        switch (errorCode) {
            case ERR_OK:
                return "Thành công";
            case ERR_SYSTEM:
            case ERR_SERVER_ERROR:
                return "Lỗi không xác định";
            case ERR_VALIDATION:
                return "Dữ liệu/tham số không hợp lệ";
            case ERR_UNAUTHORIZED:
                return "Không được truy cập hệ thống do chưa xác thực, hoặc xác thực không thành công";
            case ERR_TOKEN_INVALID:
                return "Token không đúng hoặc đã hết hạn";
            case ERR_HEADER_TOKEN_REQUIRED:
                return "Header token is required";
            case ERR_INTERNAL_SERVICE:
                return "Lỗi hệ thống nội bộ";
            case ERR_PERMISSION_INVALID:
                return "Bạn không có quyền thao tác chức năng này";
            case ERR_REQUEST_NOT_FOUND:
                return "End point url không hợp lệ !";
            case ERR_METHOD_NOT_ALLOWED:
                return "Phương thức không hợp lệ";
            case ERR_GATEWAY_ERROR:
                return "Gateway error";

            case ERR_BAD_REQUEST:
                return "Tham số không hợp lệ";
            case TOKEN_INVALID:
                return "Token không hợp lệ";
            case UNAUTHORIZED_TOKEN:
                return "Unauthorized token";
            case ACCESS_DENIED:
                return "Không có quyền truy cập ";
            case USER_EXISTED:
                return "Người dùng đã tồn tại";
            case FAILED_PASSWORD:
                return "Tài khoản hoặc mật khẩu không chính xác";
            default:
                return "Lỗi không xác định";

        }
    }
}
