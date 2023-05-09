package com.festival.domain.info.festivalPub.repository;

import com.festival.domain.info.festivalPub.data.dto.request.PubSearchCond;
import com.festival.domain.info.festivalPub.data.dto.response.PubResponse;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PubRepositoryCustom {

    Page<Pub> findByIdPubs(PubSearchCond cond, Pageable pageable);
    Page<PubResponse> findByIdPubsResponse(PubSearchCond cond, Pageable pageable);
}
