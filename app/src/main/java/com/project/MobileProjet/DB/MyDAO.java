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

    @Query("SELECT * FROM users WHERE id <> :id")
    List<User> getAllExcept(int id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void addUser(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    public User getUser(int id);

    @Query ("update users set score = users.score +:score where id = :id")
    public void addScore(int score, int id);

    @Insert
    long[] insertAll(User... user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM scoreDuels WHERE idJ1 = :id OR idJ2 = :id")
    List<ScoreDuel> getAllScoresDuelsFromPlayer(int id);

    @Insert
    void addScoreDuel(ScoreDuel scoreDuel);


}

