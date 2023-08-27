package com.festival.old.domain.fleaMarket.repository;

import com.festival.old.domain.fleaMarket.data.entity.FleaMarket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleaMarketRepository extends JpaRepository<FleaMarket, Long>, FleaMarketRepositoryCustom {
}
