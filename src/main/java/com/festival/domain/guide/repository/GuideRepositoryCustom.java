package com.festival.domain.guide.repository;

import com.festival.domain.guide.model.Guide;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GuideRepositoryCustom {
    List<Guide> getList(String status, Pageable pageable);
}
