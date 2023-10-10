package com.festival.common.util;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.InvalidException;
import com.festival.domain.bambooforest.dto.BamBooForestReq;
import com.festival.domain.booth.controller.dto.BoothReq;
import com.festival.domain.guide.dto.GuideReq;
import com.festival.domain.program.dto.ProgramReq;
import com.festival.domain.timetable.dto.TimeTableReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ValidationUtils {

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

    private static final String DATE_PATTERN =
            "^([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]$";

    public void isGuideValid(GuideReq guide) {
        checkStrLength(guide.getContent(), 1, 500);
        String[] typeList = {"NOTICE"};
    }

    public void isBoothValid(BoothReq booth) {
        checkStrLength(booth.getTitle(), 1, 30);
        checkStrLength(booth.getSubTitle(), 1, 50);
        checkStrLength(booth.getContent(), 1, 800);
        checkNumberRange(booth.getLatitude(), -90.0, 90.0);
        checkNumberRange(booth.getLongitude(), -180, 180);
        String[] typeList = {"FOOD_TRUCK", "FLEA_MARKET", "PUB"};
        checkTypeItem(typeList, booth.getType());
        checkStatus(booth.getOperateStatus());
    }

    public void isProgramValid(ProgramReq program) {
        checkStrLength(program.getTitle(), 1, 30);
        checkStrLength(program.getSubTitle(), 1, 50);
        checkStrLength(program.getContent(), 1, 800);
        checkNumberRange(program.getLatitude(), -90.0, 90.0);
        checkNumberRange(program.getLongitude(), -180, 180);
        String[] typeList = {"EVENT", "GAME"};
        checkTypeItem(typeList, program.getType());
        checkStatus(program.getOperateStatus());
    }

    public void isTimeTableValid(TimeTableReq timeTable) {
        checkItemPattern(timeTable.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), DATE_PATTERN);
        checkItemPattern(timeTable.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), DATE_PATTERN);
        checkStrLength(timeTable.getTitle(), 1, 30);
    }

    public void isBamBooForestValid(BamBooForestReq bamBooForest) {
        checkStrLength(bamBooForest.getContent(), 1, 200);
        if (!bamBooForest.getContact().isBlank()) {
            checkItemPattern(bamBooForest.getContact(), EMAIL_REGEX);
        }
    }

    private void checkStrLength(String str, int min, int max) {
        int strlen = str.length();
        if (min > strlen || max < strlen) {
            throw new InvalidException(ErrorCode.INVALID_LENGTH);
        }
    }

    private void checkNumberRange(double number, double min, double max) {
        if (min > number || max < number) {
            throw new InvalidException(ErrorCode.INVALID_RANGE);
        }
    }

    private void checkTypeItem(String[] typeList, String type) {
        boolean isExist = Arrays.asList(typeList).contains(type);
        if (!isExist) {
            throw new InvalidException(ErrorCode.INVALID_TYPE);
        }
    }

    private void checkStatus(String status) {
        String[] statusList = {"OPERATE", "TERMINATE"};
        if (!Arrays.asList(statusList).contains(status)) {
            throw new InvalidException(ErrorCode.INVALID_STATUS);
        }
    }

    private void checkItemPattern(String item, String itemPattern) {
        Pattern pattern = Pattern.compile(itemPattern);
        if (!pattern.matcher(item).matches()) {
            switch (itemPattern) {
                case DATE_PATTERN -> throw new InvalidException(ErrorCode.INVALID_DATE);
                case EMAIL_REGEX -> throw new InvalidException(ErrorCode.INVALID_EMAIL);
            }
        }
    }
}
