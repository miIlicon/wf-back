package com.festival.common.infra.Alert.discord;

import com.festival.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordService {

    @Value("${webhook.discord.url}")
    private String discordUrl;

    public void sendDiscordAlertLog(CustomException ex, HttpServletRequest request) {
        try {
            DiscordUtil discordWebhookBot = new DiscordUtil(discordUrl);
            String registeredTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

            discordWebhookBot.setUsername("Millicon");
            discordWebhookBot.setAvatarUrl("https://raw.githubusercontent.com/angelSooho/InfraTest/main/images/millicon-black.png");

            DiscordUtil.EmbedObject embedObject = new DiscordUtil.EmbedObject()
                    .setTitle("\uD83D\uDEA8 서버에 에러가 감지되었습니다. 즉시 확인이 필요합니다. \uD83D\uDEA8")
                    .setColor(Color.RED)
//                    .setFooter("여기는 footer 입니다 ", "https://i.imgur.com/Hv0xNBm.jpeg") //  푸터
//                    .setThumbnail("https://i.imgur.com/oBPXx0D.png") //  썸네일 이미지
//                    .setImage("https://i.imgur.com/8nLFCVP.png") //  메인 이미지
                    .addField("Request IP", request.getRemoteAddr(), true)
                    .addField("Request URL", request.getRequestURL() + "   " + request.getMethod(), true)
                    .addField("Error Code", ex.getErrorCode().getStatus().toString(), false)
                    .addField("Error Message", ex.getErrorCode().getMessage(), true)
                    .addField("발생 시간", registeredTimeFormat, false);

            discordWebhookBot.addEmbed(embedObject);
            discordWebhookBot.execute();
        } catch (Exception e) {
            log.debug("Discord 통신 과정에 예외 발생");
        }
    }

}
