package jp.ac.hal.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteOpener extends SQLiteOpenHelper {

  private static final int VERSION = 1;
  private static final String RC_DB_NAME = "rc_db";

  public SQLiteOpener(@Nullable Context context) {
    super(context, RC_DB_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    FileTypeTable table = new FileTypeTable(db);
    try {
      table.Create();
      table.setData();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
  }
}
