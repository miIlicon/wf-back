package com.festival.domain.info.festivalPub.repository;

import com.festival.common.vo.SearchCond;
import com.festival.domain.info.festivalPub.data.entity.pub.Pub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PubRepositoryCustom {

    Page<Pub> findByIdPubs(SearchCond cond, Pageable pageable);

    Page<Pub> findByIdPubsWithState(SearchCond cond, Pageable pageable);
}
