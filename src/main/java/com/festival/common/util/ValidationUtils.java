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
            "^([0-9]{4})-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:([0-5][0-9])$";

    public void checkStrLength(String str, int min, int max) throws Exception {
        int strlen = str.length();
        if (min > strlen || max < strlen) {
            throw new Exception("[" + str + "] 의 사이즈가 올바르지 않습니다.");
        }
    }

    public void checkNumberRange(double number, double min, double max) throws Exception {
        if (min > number || max < number) {
            throw new InvalidException(ErrorCode.INVALID_RANGE);
        }
    }

    public void checkTypeItem(String[] typeList, String type) throws Exception {
        boolean isExist = Arrays.asList(typeList).contains(type);
        if (!isExist) {
            throw new Exception("[" + type + "] 은 올바른 타입이 아닙니다.");
        }
    }

    public void checkStatus(String status) throws Exception {
        String[] statusList = {"OPERATE", "TERMINATE"};
        if (!Arrays.asList(statusList).contains(status)) {
            throw new Exception("[" + status + "] 은 올바른 상태값이 아닙니다.");
        }
    }

    private void checkItemPattern(String item, String itemPattern) {
        Pattern pattern = Pattern.compile(itemPattern);
        if (!pattern.matcher(item).matches()) {
            switch (itemPattern) {
                case EMAIL_REGEX:
                    throw new InvalidException(ErrorCode.INVALID_DATE);
                case DATE_PATTERN:
                    throw new InvalidException(ErrorCode.INVALID_EMAIL);
            }
        }
    }

    public boolean isGuideValid(GuideReq guide) throws Exception {
        checkStrLength(guide.getTitle(), 1, 20);
        checkStrLength(guide.getContent(), 1, 300);
        String[] typeList = {"NOTICE"};
        checkTypeItem(typeList, guide.getType());
        checkStatus(guide.getStatus());
        return true;
    }

    public boolean isBoothValid(BoothReq booth) throws Exception {
        checkStrLength(booth.getTitle(), 1, 30);
        checkStrLength(booth.getSubTitle(), 1, 50);
        checkStrLength(booth.getContent(), 1, 300);
        checkNumberRange(booth.getLatitude(), -90.0, 90.0);
        checkNumberRange(booth.getLongitude(), -180, 180);
        String[] typeList = {"FOOD_TRUCK", "FLEA_MARKET", "PUB"};
        checkTypeItem(typeList, booth.getType());
        checkStatus(booth.getStatus());
        return true;
    }

    public boolean isProgramValid(ProgramReq program) throws Exception {
        checkStrLength(program.getTitle(), 1, 30);
        checkStrLength(program.getSubTitle(), 1, 50);
        checkStrLength(program.getContent(), 1, 300);
        checkNumberRange(program.getLatitude(), -90.0, 90.0);
        checkNumberRange(program.getLongitude(), -180, 180);
        String[] typeList = {"EVENT", "GAME"};
        checkTypeItem(typeList, program.getType());
        checkStatus(program.getStatus());
        return true;
    }

    public boolean isTimeTableValid(TimeTableReq timeTable) throws Exception {
        checkItemPattern(timeTable.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DATE_PATTERN);
        checkItemPattern(timeTable.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DATE_PATTERN);
        checkStrLength(timeTable.getTitle(), 1, 30);
        checkStatus(timeTable.getStatus());
        return true;
    }

    public boolean isBamBooForestValid(BamBooForestReq bamBooForest) throws Exception {
        checkStrLength(bamBooForest.getContent(), 1, 200);
        checkItemPattern(bamBooForest.getContact(), EMAIL_REGEX);
        checkStatus(bamBooForest.getStatus());
        return true;
    }
}
