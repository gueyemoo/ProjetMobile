package com.project.MobileProjet.DB;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MyDAO {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void addUser(User user);

    @Query ("update users set score = users.score +:score where id = :id")
    public void addScore(int score, int id);

    @Insert
    long[] insertAll(User... user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

}
