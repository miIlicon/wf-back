package com.festival.old.domain.fleaMarket.repository;

import com.festival.old.common.vo.SearchCond;
import com.festival.old.domain.fleaMarket.data.entity.FleaMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FleaMarketRepositoryCustom {

    Page<FleaMarket> findByIdFleaMarkets(SearchCond cond, Pageable pageable);
}
