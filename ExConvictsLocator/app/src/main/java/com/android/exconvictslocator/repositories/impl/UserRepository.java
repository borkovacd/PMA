package com.android.exconvictslocator.repositories.impl;

import com.android.exconvictslocator.entities.User;
import com.android.exconvictslocator.entities.daos.UserDao;
import com.android.exconvictslocator.repositories.IUserRepository;

import java.util.List;

import io.reactivex.Flowable;

public class UserRepository implements IUserRepository {

    private static UserDao userDao;
    private static UserRepository instance;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    public static UserRepository getInstance(UserDao userDao){
if(instance == null){
instance= new UserRepository(userDao);

}
return instance;
    }


    @Override
    public Flowable<User> getByEmail(String email) {
        return userDao.getByEmail(email);
    }

    @Override
    public Long insertUser(User user) {
        return userDao.insertUser(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userDao.getUsers();
    }


}
