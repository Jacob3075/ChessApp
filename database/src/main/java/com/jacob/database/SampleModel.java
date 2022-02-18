package com.jacob.database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sample_models")
public class SampleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long attribute1;

    private String attribute2;
    private String attribute3;
    private String attribute4;

    public SampleModel(Long attribute1, String attribute2, String attribute3, String attribute4) {
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
    }

    public SampleModel() {}

    public Long getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(Long attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SampleModel) obj;
        return Objects.equals(this.attribute1, that.attribute1)
                && Objects.equals(this.attribute2, that.attribute2)
                && Objects.equals(this.attribute3, that.attribute3)
                && Objects.equals(this.attribute4, that.attribute4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute1, attribute2, attribute3, attribute4);
    }

    @Override
    public String toString() {
        return "SampleModel(attribute1=%d, attribute2=%s, attribute3=%s, attribute4=%s)"
                .formatted(attribute1, attribute2, attribute3, attribute4);
    }
}
