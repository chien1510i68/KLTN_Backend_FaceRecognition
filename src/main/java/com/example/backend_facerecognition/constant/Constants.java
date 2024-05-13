package com.example.backend_facerecognition.constant;

public interface Constants {
    interface ErrorMessageUserValidation {
        String USER_NOT_FOUND = "Không tìm thấy người dùng ";
        String ROLE_NOT_FOUND = "Không tìm thấy vai trò ";
        String USER_HAVE_EXISTED = "Người dùng đã tồn tại" ;
        String INFOR_USER_NULL = "Dữ liệu người dùng chưa chính xác";
        String USER_NOT_PERMISSION = "Không có quyền truy cập";
    }

    interface DateTimeFormatConstant {
        String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
        String DATE_FORMAT = "dd/MM/yyyy";
        String TIME_ZONE = "Asia/Ho_Chi_Minh";
    }

    interface RecognitionState {
        int SUCCESS = 1;
        int FAILED = 2;
    }
    interface ErrorMessageRoleValidation {
        String ROLE_NOT_FOUND = "Vai trò không tồn tại trong hệ thống";
    }

    interface ErrorMessageClassroomValidation {
        String CLASSROOM_NOT_FOUND = "Lớp học không tồn tại trong hệ thống";
        String SEMESTER_AND_SCHOOLYEAR_EXISTED = "Lớp học và học kì đã tồn tại";
    }
    interface ErrorCodeFile{
        String  FILE_NOT_FOUND = "Không tìm thấy file ";
    }
    interface  ErrorMessageFaceRecognition{
        String USER_NOT_IN_CLASSROOM = "Người dùng không tồn tại trong lớp học ";
        String FACE_RECOGNITION_NOT_FOUND = "Không tồn tại đối tượng " ;

        String NOT_EXITS_INPUT_IMAGE ="Không tìm thấy ảnh đầu vào";

        String FACERECOG_SECTION_EXISTED = "Phiên nhận dạng đã tồn tại" ;

        String OUTSIDE_ALLOWABLE_TIME = "Phiên nhận dạng nằm ngoài thời gian cho phép ";
        String OUTSIDE_OF_ALLOWABLE_RANGE = "Nằm ngoài phạm vi cho phép" ;
    }
    interface ErrorMessageQRCode {
        String  QRCODE_NOT_FOUND = "Không tồn tại QR code ";
        String  QRCODE_NOT_ACTIVE = "Mã QR này hiện không sử dụng được nữa ";
    }
   interface ErrorMessageCheckins{
        String CHECKIN_NOT_FOUND = "Không tồn tại Checkin" ;
        String CHECKIN_IS_EXIST = "Bạn đã điểm danh buổi học hôm nay";
   }
}
