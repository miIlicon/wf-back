package com.festival.domain.info.festivalPub.repository;

import com.festival.domain.info.festivalPub.data.dto.request.PubSearchCond;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PubRepositoryCustom {

    Page<PubResponse> findByIdPubs(PubSearchCond cond, Pageable pageable);
}
