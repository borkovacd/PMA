package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.ExConvictReport;

public interface IExConvictReportRepository {

    public Long insertExConvictReport(ExConvictReport exConvictReport);

    public void  deleteExConvictReport(ExConvictReport exConvictReport);

    /*
    public List<ExConvictReport> findAllByExConvict(int idExConvict);
    */

    public void updateExConvictReport(ExConvictReport exConvictReport);
}
