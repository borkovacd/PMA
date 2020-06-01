package com.android.exconvictslocator.repositories.impl;

import com.android.exconvictslocator.entities.ExConvict;
import com.android.exconvictslocator.entities.daos.ExConvictDao;
import com.android.exconvictslocator.repositories.IExConvictRepository;

public class ExConvictRepository implements IExConvictRepository {
    private static ExConvictDao exConvictDao;
    private static ExConvictRepository instance;

    public ExConvictRepository(ExConvictDao exConvictDao) {
        this.exConvictDao = exConvictDao;
    }

    public static ExConvictRepository getInstance(ExConvictDao exConvictDao){
        if(instance == null){
            instance= new ExConvictRepository(exConvictDao);

        }
        return instance;
    }


    @Override
    public Long insertExConvict(ExConvict exConvict) {
        return exConvictDao.insertExConvict(exConvict);
    }

    @Override
    public void deleteExConvict(ExConvict exConvict) {
exConvictDao.deleteExConvict(exConvict);
    }
}
