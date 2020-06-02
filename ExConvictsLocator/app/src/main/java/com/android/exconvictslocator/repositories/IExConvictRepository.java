package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.ExConvict;

import java.util.List;

public interface IExConvictRepository {
    public Long insertExConvict(ExConvict exConvict);
    public void deleteExConvict(ExConvict exConvict);
    public List<ExConvict> getExConvicts();

}
