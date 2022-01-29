package com.kimry.baedal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_INVALID(HttpStatus.BAD_REQUEST,"올바른 이메일을 입력해주세요"),
    USER_TYPE_INVALID(HttpStatus.BAD_REQUEST, "올바른 UserType을 입력해주세요"),
    ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 가입된 이메일입니다"),
    SIGN_IN_INVALID(HttpStatus.BAD_REQUEST, "아이디와 비밀번호를 확인해주세요"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다"),
    CUSTOMER_CANNOT_ACCEPT(HttpStatus.FORBIDDEN, "기사만 배차 요청을 수락할 수 있습니다"),
    NOT_FOUND_REQUEST(HttpStatus.NOT_FOUND, "존재하지 않는 배차 요청입니다"),
    NOT_STANDBY(HttpStatus.CONFLICT, "수락할 수 없는 배차 요청입니다. 다른 배차 요청을 선택하세요"),
    ADDRESS_INVALID(HttpStatus.BAD_REQUEST, "주소는 100자 이하로 입력해주세요"),
    DRIVER_CANNOT_REQUEST(HttpStatus.FORBIDDEN, "고객만 배차 요청할 수 있습니다"),
    ALREADY_REQUEST(HttpStatus.CONFLICT,"아직 대기중인 배차 요청이 있습니다"),
    CUSTOMER_CANNOT_COMPLETE(HttpStatus.FORBIDDEN,"기사만 배차 요청을 완료할 수 있습니다"),
    NOT_MATCH_DRIVER(HttpStatus.CONFLICT,"담당기사만 배차 요청을 완료할 수 있습니다"),
    NOT_ACCEPTED(HttpStatus.CONFLICT,"수락 상태의 요청만 완료할 수 있습니다");


    private HttpStatus status;
    private String message;
}
