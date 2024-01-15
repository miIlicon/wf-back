package com.festival.domain.guide.notice.repository;

import com.festival.domain.guide.notice.dto.NoticePageRes;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    NoticePageRes getList(Pageable pageable);
}
