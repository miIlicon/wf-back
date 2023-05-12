package com.festival.domain.fleaMarket.repository;

import com.festival.domain.fleaMarket.data.entity.FleaMarket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleaMarketRepository extends JpaRepository<FleaMarket, Long>, FleaMarketRepositoryCustom {
}
