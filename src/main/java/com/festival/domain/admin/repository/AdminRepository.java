package com.festival.domain.admin.repository;

import com.festival.domain.admin.data.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
