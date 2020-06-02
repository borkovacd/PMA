package com.android.exconvictslocator.entities.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.exconvictslocator.entities.ExConvict;

import java.util.List;

@Dao
public interface ExConvictDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertExConvict(ExConvict exConvict);

    @Delete
    public void deleteExConvict(ExConvict exConvict);


    @Query("SELECT * FROM ExConvict")
    public List<ExConvict> getExConvicts();
}
