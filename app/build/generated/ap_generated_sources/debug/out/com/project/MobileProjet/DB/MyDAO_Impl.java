package com.project.MobileProjet.DB;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class MyDAO_Impl implements MyDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfUser;

  private final EntityInsertionAdapter __insertionAdapterOfUser_1;

  private final EntityInsertionAdapter __insertionAdapterOfScoreDuel;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfAddScore;

  public MyDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `users`(`pseudo`,`id`,`age`,`sexe`,`photo`,`score`) VALUES (?,nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        if (value.getPseudo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPseudo());
        }
        stmt.bindLong(2, value.getId());
        stmt.bindLong(3, value.getAge());
        if (value.getSexe() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSexe());
        }
        stmt.bindLong(5, value.getPhoto());
        stmt.bindLong(6, value.getScore());
      }
    };
    this.__insertionAdapterOfUser_1 = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `users`(`pseudo`,`id`,`age`,`sexe`,`photo`,`score`) VALUES (?,nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        if (value.getPseudo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPseudo());
        }
        stmt.bindLong(2, value.getId());
        stmt.bindLong(3, value.getAge());
        if (value.getSexe() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSexe());
        }
        stmt.bindLong(5, value.getPhoto());
        stmt.bindLong(6, value.getScore());
      }
    };
    this.__insertionAdapterOfScoreDuel = new EntityInsertionAdapter<ScoreDuel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `scoreDuels`(`id`,`idJ1`,`idJ2`,`exercice`,`scoreJ1`,`scoreJ2`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ScoreDuel value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getIdJ1());
        stmt.bindLong(3, value.getIdJ2());
        if (value.getExercice() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getExercice());
        }
        stmt.bindLong(5, value.getScoreJ1());
        stmt.bindLong(6, value.getScoreJ2());
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `users` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `users` SET `pseudo` = ?,`id` = ?,`age` = ?,`sexe` = ?,`photo` = ?,`score` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        if (value.getPseudo() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPseudo());
        }
        stmt.bindLong(2, value.getId());
        stmt.bindLong(3, value.getAge());
        if (value.getSexe() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSexe());
        }
        stmt.bindLong(5, value.getPhoto());
        stmt.bindLong(6, value.getScore());
        stmt.bindLong(7, value.getId());
      }
    };
    this.__preparedStmtOfAddScore = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "update users set score = users.score +? where id = ?";
        return _query;
      }
    };
  }

  @Override
  public void addUser(User user) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long[] insertAll(User... user) {
    __db.beginTransaction();
    try {
      long[] _result = __insertionAdapterOfUser_1.insertAndReturnIdsArray(user);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void addScoreDuel(ScoreDuel scoreDuel) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfScoreDuel.insert(scoreDuel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(User user) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(User user) {
    __db.beginTransaction();
    try {
      __updateAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void addScore(int score, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfAddScore.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      _stmt.bindLong(_argIndex, score);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfAddScore.release(_stmt);
    }
  }

  @Override
  public List<User> getAll() {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfPseudo = _cursor.getColumnIndexOrThrow("pseudo");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAge = _cursor.getColumnIndexOrThrow("age");
      final int _cursorIndexOfSexe = _cursor.getColumnIndexOrThrow("sexe");
      final int _cursorIndexOfPhoto = _cursor.getColumnIndexOrThrow("photo");
      final int _cursorIndexOfScore = _cursor.getColumnIndexOrThrow("score");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        final String _tmpPseudo;
        _tmpPseudo = _cursor.getString(_cursorIndexOfPseudo);
        _item.setPseudo(_tmpPseudo);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        _item.setAge(_tmpAge);
        final String _tmpSexe;
        _tmpSexe = _cursor.getString(_cursorIndexOfSexe);
        _item.setSexe(_tmpSexe);
        final int _tmpPhoto;
        _tmpPhoto = _cursor.getInt(_cursorIndexOfPhoto);
        _item.setPhoto(_tmpPhoto);
        final int _tmpScore;
        _tmpScore = _cursor.getInt(_cursorIndexOfScore);
        _item.setScore(_tmpScore);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<User> getAllExcept(int id) {
    final String _sql = "SELECT * FROM users WHERE id <> ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfPseudo = _cursor.getColumnIndexOrThrow("pseudo");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAge = _cursor.getColumnIndexOrThrow("age");
      final int _cursorIndexOfSexe = _cursor.getColumnIndexOrThrow("sexe");
      final int _cursorIndexOfPhoto = _cursor.getColumnIndexOrThrow("photo");
      final int _cursorIndexOfScore = _cursor.getColumnIndexOrThrow("score");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        final String _tmpPseudo;
        _tmpPseudo = _cursor.getString(_cursorIndexOfPseudo);
        _item.setPseudo(_tmpPseudo);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        _item.setAge(_tmpAge);
        final String _tmpSexe;
        _tmpSexe = _cursor.getString(_cursorIndexOfSexe);
        _item.setSexe(_tmpSexe);
        final int _tmpPhoto;
        _tmpPhoto = _cursor.getInt(_cursorIndexOfPhoto);
        _item.setPhoto(_tmpPhoto);
        final int _tmpScore;
        _tmpScore = _cursor.getInt(_cursorIndexOfScore);
        _item.setScore(_tmpScore);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User getUser(int id) {
    final String _sql = "SELECT * FROM users WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfPseudo = _cursor.getColumnIndexOrThrow("pseudo");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfAge = _cursor.getColumnIndexOrThrow("age");
      final int _cursorIndexOfSexe = _cursor.getColumnIndexOrThrow("sexe");
      final int _cursorIndexOfPhoto = _cursor.getColumnIndexOrThrow("photo");
      final int _cursorIndexOfScore = _cursor.getColumnIndexOrThrow("score");
      final User _result;
      if(_cursor.moveToFirst()) {
        _result = new User();
        final String _tmpPseudo;
        _tmpPseudo = _cursor.getString(_cursorIndexOfPseudo);
        _result.setPseudo(_tmpPseudo);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        _result.setAge(_tmpAge);
        final String _tmpSexe;
        _tmpSexe = _cursor.getString(_cursorIndexOfSexe);
        _result.setSexe(_tmpSexe);
        final int _tmpPhoto;
        _tmpPhoto = _cursor.getInt(_cursorIndexOfPhoto);
        _result.setPhoto(_tmpPhoto);
        final int _tmpScore;
        _tmpScore = _cursor.getInt(_cursorIndexOfScore);
        _result.setScore(_tmpScore);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ScoreDuel> getAllScoresDuelsFromPlayer(int id) {
    final String _sql = "SELECT * FROM scoreDuels WHERE idJ1 = ? OR idJ2 = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    _argIndex = 2;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfIdJ1 = _cursor.getColumnIndexOrThrow("idJ1");
      final int _cursorIndexOfIdJ2 = _cursor.getColumnIndexOrThrow("idJ2");
      final int _cursorIndexOfExercice = _cursor.getColumnIndexOrThrow("exercice");
      final int _cursorIndexOfScoreJ1 = _cursor.getColumnIndexOrThrow("scoreJ1");
      final int _cursorIndexOfScoreJ2 = _cursor.getColumnIndexOrThrow("scoreJ2");
      final List<ScoreDuel> _result = new ArrayList<ScoreDuel>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ScoreDuel _item;
        final int _tmpIdJ1;
        _tmpIdJ1 = _cursor.getInt(_cursorIndexOfIdJ1);
        final int _tmpIdJ2;
        _tmpIdJ2 = _cursor.getInt(_cursorIndexOfIdJ2);
        final int _tmpScoreJ1;
        _tmpScoreJ1 = _cursor.getInt(_cursorIndexOfScoreJ1);
        final int _tmpScoreJ2;
        _tmpScoreJ2 = _cursor.getInt(_cursorIndexOfScoreJ2);
        _item = new ScoreDuel(_tmpIdJ1,_tmpIdJ2,_tmpScoreJ1,_tmpScoreJ2);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpExercice;
        _tmpExercice = _cursor.getString(_cursorIndexOfExercice);
        _item.setExercice(_tmpExercice);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
