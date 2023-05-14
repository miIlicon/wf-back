package com.festival.domain.fleaMarket.repository;

import com.festival.common.vo.SearchCond;
import com.festival.domain.fleaMarket.data.entity.FleaMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FleaMarketRepositoryCustom {

    Page<FleaMarket> findByIdFleaMarkets(SearchCond cond, Pageable pageable);
}
