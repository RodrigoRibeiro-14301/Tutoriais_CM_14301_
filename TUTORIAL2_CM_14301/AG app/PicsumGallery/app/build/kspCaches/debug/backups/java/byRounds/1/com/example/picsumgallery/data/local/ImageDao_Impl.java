package com.example.picsumgallery.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ImageDao_Impl implements ImageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ImageEntity> __insertionAdapterOfImageEntity;

  private final SharedSQLiteStatement __preparedStmtOfSetFavorite;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOldestCacheEntry;

  private final SharedSQLiteStatement __preparedStmtOfClearCache;

  public ImageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfImageEntity = new EntityInsertionAdapter<ImageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `images` (`id`,`author`,`width`,`height`,`url`,`downloadUrl`,`isFavorite`,`favoriteTimestamp`,`cacheTimestamp`,`cachePosition`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ImageEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getAuthor());
        statement.bindLong(3, entity.getWidth());
        statement.bindLong(4, entity.getHeight());
        statement.bindString(5, entity.getUrl());
        statement.bindString(6, entity.getDownloadUrl());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getFavoriteTimestamp());
        statement.bindLong(9, entity.getCacheTimestamp());
        statement.bindLong(10, entity.getCachePosition());
      }
    };
    this.__preparedStmtOfSetFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE images SET isFavorite = ?, favoriteTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOldestCacheEntry = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM images WHERE id IN (SELECT id FROM images WHERE isFavorite = 0 ORDER BY cacheTimestamp ASC LIMIT 1)";
        return _query;
      }
    };
    this.__preparedStmtOfClearCache = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM images WHERE isFavorite = 0";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<ImageEntity> images,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfImageEntity.insert(images);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setFavorite(final String id, final boolean isFavorite, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOldestCacheEntry(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOldestCacheEntry.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOldestCacheEntry.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearCache(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearCache.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearCache.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCached(final Continuation<? super List<ImageEntity>> $completion) {
    final String _sql = "SELECT * FROM images ORDER BY cachePosition ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ImageEntity>>() {
      @Override
      @NonNull
      public List<ImageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfFavoriteTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteTimestamp");
          final int _cursorIndexOfCacheTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTimestamp");
          final int _cursorIndexOfCachePosition = CursorUtil.getColumnIndexOrThrow(_cursor, "cachePosition");
          final List<ImageEntity> _result = new ArrayList<ImageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ImageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final long _tmpFavoriteTimestamp;
            _tmpFavoriteTimestamp = _cursor.getLong(_cursorIndexOfFavoriteTimestamp);
            final long _tmpCacheTimestamp;
            _tmpCacheTimestamp = _cursor.getLong(_cursorIndexOfCacheTimestamp);
            final int _tmpCachePosition;
            _tmpCachePosition = _cursor.getInt(_cursorIndexOfCachePosition);
            _item = new ImageEntity(_tmpId,_tmpAuthor,_tmpWidth,_tmpHeight,_tmpUrl,_tmpDownloadUrl,_tmpIsFavorite,_tmpFavoriteTimestamp,_tmpCacheTimestamp,_tmpCachePosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<ImageEntity>> getFavorites() {
    final String _sql = "SELECT * FROM images WHERE isFavorite = 1 ORDER BY favoriteTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"images"}, false, new Callable<List<ImageEntity>>() {
      @Override
      @Nullable
      public List<ImageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfFavoriteTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteTimestamp");
          final int _cursorIndexOfCacheTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTimestamp");
          final int _cursorIndexOfCachePosition = CursorUtil.getColumnIndexOrThrow(_cursor, "cachePosition");
          final List<ImageEntity> _result = new ArrayList<ImageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ImageEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final long _tmpFavoriteTimestamp;
            _tmpFavoriteTimestamp = _cursor.getLong(_cursorIndexOfFavoriteTimestamp);
            final long _tmpCacheTimestamp;
            _tmpCacheTimestamp = _cursor.getLong(_cursorIndexOfCacheTimestamp);
            final int _tmpCachePosition;
            _tmpCachePosition = _cursor.getInt(_cursorIndexOfCachePosition);
            _item = new ImageEntity(_tmpId,_tmpAuthor,_tmpWidth,_tmpHeight,_tmpUrl,_tmpDownloadUrl,_tmpIsFavorite,_tmpFavoriteTimestamp,_tmpCacheTimestamp,_tmpCachePosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOldestFavorite(final Continuation<? super ImageEntity> $completion) {
    final String _sql = "SELECT * FROM images WHERE isFavorite = 1 ORDER BY favoriteTimestamp ASC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ImageEntity>() {
      @Override
      @Nullable
      public ImageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfFavoriteTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteTimestamp");
          final int _cursorIndexOfCacheTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTimestamp");
          final int _cursorIndexOfCachePosition = CursorUtil.getColumnIndexOrThrow(_cursor, "cachePosition");
          final ImageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final long _tmpFavoriteTimestamp;
            _tmpFavoriteTimestamp = _cursor.getLong(_cursorIndexOfFavoriteTimestamp);
            final long _tmpCacheTimestamp;
            _tmpCacheTimestamp = _cursor.getLong(_cursorIndexOfCacheTimestamp);
            final int _tmpCachePosition;
            _tmpCachePosition = _cursor.getInt(_cursorIndexOfCachePosition);
            _result = new ImageEntity(_tmpId,_tmpAuthor,_tmpWidth,_tmpHeight,_tmpUrl,_tmpDownloadUrl,_tmpIsFavorite,_tmpFavoriteTimestamp,_tmpCacheTimestamp,_tmpCachePosition);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCacheCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM images WHERE isFavorite = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id, final Continuation<? super ImageEntity> $completion) {
    final String _sql = "SELECT * FROM images WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ImageEntity>() {
      @Override
      @Nullable
      public ImageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfWidth = CursorUtil.getColumnIndexOrThrow(_cursor, "width");
          final int _cursorIndexOfHeight = CursorUtil.getColumnIndexOrThrow(_cursor, "height");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfFavoriteTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteTimestamp");
          final int _cursorIndexOfCacheTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "cacheTimestamp");
          final int _cursorIndexOfCachePosition = CursorUtil.getColumnIndexOrThrow(_cursor, "cachePosition");
          final ImageEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final int _tmpWidth;
            _tmpWidth = _cursor.getInt(_cursorIndexOfWidth);
            final int _tmpHeight;
            _tmpHeight = _cursor.getInt(_cursorIndexOfHeight);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final String _tmpDownloadUrl;
            _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final long _tmpFavoriteTimestamp;
            _tmpFavoriteTimestamp = _cursor.getLong(_cursorIndexOfFavoriteTimestamp);
            final long _tmpCacheTimestamp;
            _tmpCacheTimestamp = _cursor.getLong(_cursorIndexOfCacheTimestamp);
            final int _tmpCachePosition;
            _tmpCachePosition = _cursor.getInt(_cursorIndexOfCachePosition);
            _result = new ImageEntity(_tmpId,_tmpAuthor,_tmpWidth,_tmpHeight,_tmpUrl,_tmpDownloadUrl,_tmpIsFavorite,_tmpFavoriteTimestamp,_tmpCacheTimestamp,_tmpCachePosition);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
