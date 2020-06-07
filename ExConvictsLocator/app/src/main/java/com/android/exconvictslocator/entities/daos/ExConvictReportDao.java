package com.android.exconvictslocator.entities.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.android.exconvictslocator.entities.ExConvictReport;

@Dao
public interface ExConvictReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertExConvictReport(ExConvictReport exConvictReport);

    @Delete
    public void deleteExConvictReport(ExConvictReport exConvictReport);

    /*
    @Query("SELECT * FROM Ex where email = :email")
    public List<ExConvictReport> findExConvictReportsByExConvict(int exConvictId);
    */

    @Update
    void update(ExConvictReport exConvictReport);
}
