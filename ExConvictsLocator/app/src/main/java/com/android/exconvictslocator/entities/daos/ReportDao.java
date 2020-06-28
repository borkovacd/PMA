package com.android.exconvictslocator.entities.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.exconvictslocator.entities.Report;

import java.util.List;

@Dao
public interface ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertReport(Report report);

    @Delete
    public void deleteReport(Report report);

    @Query("SELECT * FROM Report where isSync = 1")
    public List<Report> getNotSyncedReports();

    @Update
    void update(Report report);

}
