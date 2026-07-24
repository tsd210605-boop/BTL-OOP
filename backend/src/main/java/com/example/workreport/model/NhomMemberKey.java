package com.example.workreport.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class NhomMemberKey implements Serializable {
    private String nhomID;
    private String sinhVienID;

    public NhomMemberKey() {}

    public NhomMemberKey(String nhomID, String sinhVienID) {
        this.nhomID = nhomID;
        this.sinhVienID = sinhVienID;
    }

    public String getNhomID() {
        return nhomID;
    }
    public void setNhomID(String nhomID) {
        this.nhomID = nhomID;
    }
    public String getSinhVienID() {
        return sinhVienID;
    }
    public void setSinhVienID(String sinhVienID) {
        this.sinhVienID = sinhVienID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NhomMemberKey)) return false;
        NhomMemberKey that = (NhomMemberKey) o;
        return Objects.equals(nhomID, that.nhomID) && Objects.equals(sinhVienID, that.sinhVienID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nhomID, sinhVienID);
    }
}

