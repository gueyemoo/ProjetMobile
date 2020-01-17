package com.project.MobileProjet.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class MyDB_Impl extends MyDB {
  private volatile MyDAO _myDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`pseudo` TEXT, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `age` INTEGER NOT NULL, `sexe` TEXT, `photo` INTEGER NOT NULL, `score` INTEGER NOT NULL)");
        _db.execSQL("CREATE UNIQUE INDEX `index_users_pseudo` ON `users` (`pseudo`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `scoreDuels` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `idJ1` INTEGER NOT NULL, `idJ2` INTEGER NOT NULL, `exercice` TEXT, `scoreJ1` INTEGER NOT NULL, `scoreJ2` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"651dbfe7d4ca848982c71ff75c47689a\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `users`");
        _db.execSQL("DROP TABLE IF EXISTS `scoreDuels`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("pseudo", new TableInfo.Column("pseudo", "TEXT", false, 0));
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsUsers.put("age", new TableInfo.Column("age", "INTEGER", true, 0));
        _columnsUsers.put("sexe", new TableInfo.Column("sexe", "TEXT", false, 0));
        _columnsUsers.put("photo", new TableInfo.Column("photo", "INTEGER", true, 0));
        _columnsUsers.put("score", new TableInfo.Column("score", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(1);
        _indicesUsers.add(new TableInfo.Index("index_users_pseudo", true, Arrays.asList("pseudo")));
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(_db, "users");
        if (! _infoUsers.equals(_existingUsers)) {
          throw new IllegalStateException("Migration didn't properly handle users(com.project.MobileProjet.DB.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsScoreDuels = new HashMap<String, TableInfo.Column>(6);
        _columnsScoreDuels.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsScoreDuels.put("idJ1", new TableInfo.Column("idJ1", "INTEGER", true, 0));
        _columnsScoreDuels.put("idJ2", new TableInfo.Column("idJ2", "INTEGER", true, 0));
        _columnsScoreDuels.put("exercice", new TableInfo.Column("exercice", "TEXT", false, 0));
        _columnsScoreDuels.put("scoreJ1", new TableInfo.Column("scoreJ1", "INTEGER", true, 0));
        _columnsScoreDuels.put("scoreJ2", new TableInfo.Column("scoreJ2", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScoreDuels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScoreDuels = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScoreDuels = new TableInfo("scoreDuels", _columnsScoreDuels, _foreignKeysScoreDuels, _indicesScoreDuels);
        final TableInfo _existingScoreDuels = TableInfo.read(_db, "scoreDuels");
        if (! _infoScoreDuels.equals(_existingScoreDuels)) {
          throw new IllegalStateException("Migration didn't properly handle scoreDuels(com.project.MobileProjet.DB.ScoreDuel).\n"
                  + " Expected:\n" + _infoScoreDuels + "\n"
                  + " Found:\n" + _existingScoreDuels);
        }
      }
    }, "651dbfe7d4ca848982c71ff75c47689a", "0da6c2afec57dc7669eedc77d70fcdfb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "users","scoreDuels");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `scoreDuels`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public MyDAO mydao() {
    if (_myDAO != null) {
      return _myDAO;
    } else {
      synchronized(this) {
        if(_myDAO == null) {
          _myDAO = new MyDAO_Impl(this);
        }
        return _myDAO;
      }
    }
  }
}
