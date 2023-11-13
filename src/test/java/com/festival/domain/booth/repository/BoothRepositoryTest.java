package com.festival.domain.booth.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class BoothRepositoryTest {

    @Autowired
    private BoothRepository boothRepository;

}