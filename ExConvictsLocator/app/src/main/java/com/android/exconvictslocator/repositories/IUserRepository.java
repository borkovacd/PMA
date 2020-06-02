package com.android.exconvictslocator.repositories;

import com.android.exconvictslocator.entities.User;

import java.util.List;

import io.reactivex.Flowable;

public interface IUserRepository {
    public Flowable<User> getByEmail(String email);

    public Long insertUser(User user);

    public void deleteUser(User user);

    public List<User> getUsers();

    User findUserByEmail(String email);

    void updateUser(User user);
}
