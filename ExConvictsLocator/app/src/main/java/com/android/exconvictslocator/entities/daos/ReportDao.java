package com.android.exconvictslocator.entities.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.android.exconvictslocator.entities.Report;

@Dao
public interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertReport(Report report);

    @Delete
    public void deleteReport(Report report);
}
