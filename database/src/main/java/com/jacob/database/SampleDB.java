package com.jacob.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDB extends JpaRepository<SampleModel, Long> {}
