package com.android.exconvictslocator.entities.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.exconvictslocator.entities.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User where email = :email")
    public Flowable<User> getByEmail(String email);

    @Query("SELECT * FROM User where email = :email")
    public User findUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("SELECT * FROM User")
    public List<User> getUsers();

    @Update
    void update(User user);

    @Query("SELECT * FROM User where isSync = 0")
    public List<User> getNotSyncedUsers();
}
