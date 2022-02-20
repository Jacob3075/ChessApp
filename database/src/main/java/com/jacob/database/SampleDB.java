package com.jacob.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

interface SampleRepository extends JpaRepository<SampleModel, Long> {}

@Repository
public class SampleDB {
    private final SampleRepository repository;

    public SampleDB(SampleRepository repository) {
        this.repository = repository;
    }

    public void saveData(SampleModel sampleModel) {
        repository.save(sampleModel);
    }
}
