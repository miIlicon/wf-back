package com.festival.common.exception;

import com.festival.common.infra.Alert.discord.DiscordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    //    private final SlackService slackService;
    private final DiscordService discordService;

    /**
     * 서비스 로직 도중 발생하는 에러들을 커스텀하여 응답값을 내려줍니다.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request){
        ErrorCode errorCode = e.getErrorCode();
        discordService.sendDiscordAlertLog(e, request);
        log.error(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body( new ErrorResponse(errorCode.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException() {
        String errorMessage = maxFileSize + " 이내 용량의 파일들만 업로드 할 수 있습니다.";
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(errorMessage));
    }

    /**
     * 바인딩 에러 json형식으로 에러가 있는 파라미터 값에 메시지를 담아 넘겨줍니다.
     *  itemPrice : 아이템 가격을 100원 이상 1,000,000,000원 이하여야 합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

}
