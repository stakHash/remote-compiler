package jp.ac.hal.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FileTypeTable {

  private static final String TABLE_NAME = "FileType";
  private static final String[][] FILE_TYPES = {
      {"golang", "go"},
      {"java", "java"},
//      {"kotlin", "kt"},
//      {"python", "py"},
  };

  private SQLiteDatabase db;

  public FileTypeTable(SQLiteDatabase db) {
    this.db = db;
  }

  void Create() throws SQLException {
    String sql = "create table " + TABLE_NAME + "(" +
        "_id integer primary key autoincrement," +
        "type text not null unique," +
        "ext text not null" +
        ")";
    this.db.execSQL(sql);
  }

  @Nullable
  public FileType getTypeFromType(String type) {
    return getType("type", type);
  }

  @Nullable
  public FileType getTypeFromExt(String ext) {
    return getType("ext", ext);
  }

  @Nullable
  private FileType getType(String selection, String arg) {
    FileType ft = null;
    try (
        Cursor cursor = this.db.query(TABLE_NAME, null, selection + " = ?", new String[]{arg}, null, null, null)
    ) {
      if (cursor.moveToFirst()) {
        ft = new FileType(
            cursor.getInt(cursor.getColumnIndex("_id")),
            cursor.getString(cursor.getColumnIndex("type")),
            cursor.getString(cursor.getColumnIndex("ext"))
        );
      }
    }
    return ft;
  }

  public FileType[] getAllTypes() {
    ArrayList<FileType> data = new ArrayList<>();
    try (
        Cursor cursor = this.db.query(TABLE_NAME, null, null, null, null, null, null)
    ) {
      boolean b = cursor.moveToFirst();
      while (b) {
        data.add(new FileType(
            cursor.getInt(cursor.getColumnIndex("_id")),
            cursor.getString(cursor.getColumnIndex("type")),
            cursor.getString(cursor.getColumnIndex("ext"))
        ));
        b = cursor.moveToNext();
      }
    }
    FileType[] resArray = new FileType[data.size()];
    data.toArray(resArray);
    return resArray;
  }

  public void setData() throws SQLException {
    this.db.beginTransaction();
    try (
        final SQLiteStatement statement = this.db.compileStatement("insert into " + TABLE_NAME + "(type, ext) VALUES (?,?)")
    ) {
      for (String[] type : FILE_TYPES) {
        statement.bindString(1, type[0]);
        statement.bindString(2, type[1]);
        statement.executeInsert();
      }
      this.db.setTransactionSuccessful();
    } finally {
      this.db.endTransaction();
    }
  }

}
