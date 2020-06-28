package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.Report;

import java.util.List;

public interface IReportRepository {

    public Long insertReport(Report report);

    public void deleteReport(Report report);

    public List<Report> getNotSyncedReports();

    void update(Report report);

}
