package com.festival.domain.foodTruck.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jboss.logging.MDC;

@AllArgsConstructor
@NoArgsConstructor
public class FoodTruckResponse {
    private String requestId;
    private Long resourceId;

    public FoodTruckResponse(Long resourceId) {
        this.requestId = MDC.get("request-id").toString();
        this.resourceId = resourceId;
    }
}
