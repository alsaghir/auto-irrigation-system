package com.github.alsaghir.autoirrigationsystem.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlotRepository extends JpaRepository<Plot, Long> {
}
