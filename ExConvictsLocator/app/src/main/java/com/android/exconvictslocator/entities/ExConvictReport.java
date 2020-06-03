package com.android.exconvictslocator.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ExConvictReport {

    @Embedded
    public ExConvict exConvict;

    @Relation(parentColumn = "id", entityColumn = "exConvictId", entity = Report.class)
    public List<Report> reports;

    public ExConvict getExConvict() {
        return exConvict;
    }

    public void setExConvict(ExConvict exConvict) {
        this.exConvict = exConvict;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
}
