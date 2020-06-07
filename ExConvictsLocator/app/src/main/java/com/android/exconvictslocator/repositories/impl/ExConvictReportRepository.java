package com.android.exconvictslocator.repositories.impl;

import com.android.exconvictslocator.entities.ExConvictReport;
import com.android.exconvictslocator.entities.daos.ExConvictReportDao;
import com.android.exconvictslocator.repositories.IExConvictReportRepository;

import java.util.List;

public class ExConvictReportRepository implements IExConvictReportRepository {

    private static ExConvictReportDao exConvictReportDao ;
    private static ExConvictReportRepository instance ;

    public ExConvictReportRepository(ExConvictReportDao exConvictReportDao) { this.exConvictReportDao = exConvictReportDao;  }

    public static ExConvictReportRepository getInstance(ExConvictReportDao exConvictReportDao) {
        if (instance == null) {
            instance = new ExConvictReportRepository(exConvictReportDao);
        }
        return instance ;
    }

    @Override
    public Long insertExConvictReport(ExConvictReport exConvictReport) {
        return exConvictReportDao.insertExConvictReport(exConvictReport);
    }

    @Override
    public void deleteExConvictReport(ExConvictReport exConvictReport) {
        exConvictReportDao.deleteExConvictReport(exConvictReport);
    }

    /*
    @Override
    public List<ExConvictReport> findAllByExConvict(int idExConvict) {
        return exConvictReportDao.findExConvictReportsByExConvict(idExConvict);
    }
    */

    @Override
    public void updateExConvictReport(ExConvictReport exConvictReport) {
        exConvictReportDao.update(exConvictReport);
    }
}
