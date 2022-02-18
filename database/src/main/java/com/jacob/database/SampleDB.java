package com.jacob.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleDB extends JpaRepository<SampleModel, Long> {}
