package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.Report;

public interface IReportRepository {

    public Long insertReport(Report report);

    public void deleteReport(Report report);
}
