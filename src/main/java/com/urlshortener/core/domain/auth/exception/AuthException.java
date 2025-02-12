package com.urlshortener.core.domain.auth.exception;

import com.urlshortener.core.infrastucture.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthException implements ExceptionCode {
    TOKEN_EXPIRED("AUTH001", 401, "Token has expired"),
    INVALID_CREDENTIALS("AUTH002", 401, "Invalid email or password"),
    UNAUTHORIZED_ACCESS("AUTH003", 401, "Unauthorized access"),
    USER_NOT_FOUND("AUTH004", 404, "User not found"),
    TOKEN_VERIFICATION_FAILED("AUTH005", 403, "Token verification failed"),
    TOKEN_SIGNATURE_INVALID("AUTH006", 403, "Token signature is invalid"),
    EMAIL_ALREADY_EXISTS("AUTH007", 409, "Email already exists"),
    REFRESH_TOKEN_EXPIRED("AUTH008", 403, "Refresh token has expired"),
    REFRESH_TOKEN_INVALID("AUTH009", 403, "Invalid refresh token"),
    PASSWORD_MISMATCH("AUTH010", 400, "Old password is incorrect"),
    PASSWORD_TOO_WEAK("AUTH011", 400, "Password is too weak"),
    ACCOUNT_LOCKED("AUTH012", 403, "Account is locked"),
    ACCOUNT_DISABLED("AUTH013", 403, "Account is disabled"),
    ACCESS_DENIED("AUTH014", 403, "Access denied"),
    EMAIL_NOT_VERIFIED("AUTH015", 403, "Email is not verified"),
    TOKEN_MISSING("AUTH016", 400, "Token is missing"),
    TOKEN_INVALID("AUTH017", 400, "Token is invalid"),
    SESSION_EXPIRED("AUTH018", 401, "Session has expired"),
    LOGIN_REQUIRED("AUTH019", 401, "Login is required"),
    TOO_MANY_LOGIN_ATTEMPTS("AUTH020", 429, "Too many login attempts, please try again later");

    private final String type;
    private final Integer code;
    private final String message;
}
