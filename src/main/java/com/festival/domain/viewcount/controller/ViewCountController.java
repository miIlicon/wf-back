package com.festival.domain.viewcount.controller;

import com.festival.domain.viewcount.service.ViewCountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ViewCountController {
    private ViewCountService viewCountService;

    @GetMapping("/")
    public ResponseEntity<Void> increase(HttpServletRequest httpServletRequest){
        viewCountService.increase(httpServletRequest.getRemoteAddr());
        return ResponseEntity.ok().build();
    }
}
