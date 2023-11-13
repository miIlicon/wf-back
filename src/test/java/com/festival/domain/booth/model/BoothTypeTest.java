package com.festival.domain.booth.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class BoothTypeTest {

    @DisplayName("")
    @Test
    void containsBoothType1() throws Exception {
        //given
        BoothType givenType = BoothType.FOOD_TRUCK;

        //when
        boolean result = givenType.getValue().equals("FOOD_TRUCK");

        //then
        assertThat(result).isTrue();
    }

    private static Stream<Arguments> provideStringForCheckingBoothType() {
        return Stream.of(
                Arguments.of("FOOD_TRUCK", true),
                Arguments.of("FLEA_MARKET", true),
                Arguments.of("PUB", true)
        );
    }

    @DisplayName("")
    @MethodSource("provideStringForCheckingBoothType")
    @ParameterizedTest
    void containsBoothType2(String inputBoothType, boolean expected) throws Exception {
        //when
        BoothType boothType = BoothType.handleType(inputBoothType);

        //then
        assertThat(boothType.getValue().equals(inputBoothType)).isEqualTo(expected);

    }
}