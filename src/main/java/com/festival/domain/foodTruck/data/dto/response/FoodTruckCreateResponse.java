package com.festival.domain.foodTruck.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.logging.MDC;

@AllArgsConstructor
@NoArgsConstructor
public class FoodTruckCreateResponse {
    private String requestId;
    private Long resourceId;


    public FoodTruckCreateResponse(Long resourceId) {
        this.requestId = MDC.get("request-id").toString();
        this.resourceId = resourceId;
    }
}
