package com.android.exconvictslocator.repositories.impl;

import com.android.exconvictslocator.entities.Report;
import com.android.exconvictslocator.entities.daos.ExConvictDao;
import com.android.exconvictslocator.entities.daos.ReportDao;
import com.android.exconvictslocator.repositories.IReportRepository;

public class ReportRepository implements IReportRepository {
    private static ReportDao reportDao;
    private static ReportRepository instance;

    public ReportRepository(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    public static ReportRepository getInstance(ReportDao reportDao){
        if(instance == null){
            instance= new ReportRepository(reportDao);

        }
        return instance;
    }

    @Override
    public Long insertReport(Report report) {
        return reportDao.insertReport(report);
    }

    @Override
    public void deleteReport(Report report) {
reportDao.deleteReport(report);
    }
}