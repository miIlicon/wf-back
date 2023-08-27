package com.festival.domain.program.repository;

import com.festival.domain.program.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
}
