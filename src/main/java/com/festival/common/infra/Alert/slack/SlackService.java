package com.festival.common.infra.Alert.slack;

import com.festival.common.exception.CustomException;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Slf4j
@Component
public class SlackService {

    private final Slack slackClient = Slack.getInstance();

    @Value("${webhook.slack.url}")
    private String slackUrl;

    public void sendSlackAlertLog(CustomException e, HttpServletRequest request) {
        try {
            slackClient.send(slackUrl, payload(p -> p
                    .text("\uD83D\uDEA8" +
                            " 서버에 에러가 감지되었습니다. 즉시 확인이 필요합니다. " +
                            "\uD83D\uDEA8")
                    .attachments(
                            List.of(generateSlackAttachment(e, request, LocalDateTime.now()))
                    )
            ));
        } catch (IOException slackError) {
            log.debug("Slack 통신 과정에 예외 발생");
        }
    }

    private Attachment generateSlackAttachment(CustomException e, HttpServletRequest request, LocalDateTime dateTime) {
        String requestTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(dateTime);
        String xffHeader = request.getHeader("X-FORWARDED-FOR");
        return Attachment.builder()
                .color("ff0000")
                .title(requestTime + " 발생 에러 로그")
                .fields(List.of(
                                generateSlackField("Request IP", xffHeader == null ? request.getRemoteAddr() : xffHeader),
                                generateSlackField("Request URL", request.getRequestURL() + " " + request.getMethod()),
                                generateSlackField("Error Code", e.getErrorCode().getStatus().toString()),
                                generateSlackField("Error Message", e.getErrorCode().getMessage())
                        )
                )
                .build();
    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
