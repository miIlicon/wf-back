package com.festival.old.domain.info.festivalPub.repository;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.info.festivalPub.data.entity.pub.Pub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PubRepositoryCustom {

    Page<Pub> findByIdPubs(SearchCond cond, Pageable pageable);
}
