package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.ExConvictReport;

import java.util.List;

public interface IExConvictRepository {
    public Long insertExConvict(ExConvict exConvict);
    public void deleteExConvict(ExConvict exConvict);
    public List<ExConvict> getExConvicts();
    public List<ExConvictReport> getExConvictReports();
    public ExConvictReport getExConvictByIdWithReports(int id);
}
