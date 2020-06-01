package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.ExConvict;

public interface IExConvictRepository {
    public Long insertExConvict(ExConvict exConvict);
    public void deleteExConvict(ExConvict exConvict);
}
