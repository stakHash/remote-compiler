package jp.ac.hal.database;

import android.support.annotation.NonNull;

public class FileType {

  private int id;
  private String type;
  private String ext;

  public FileType() {
  }

  FileType(int id, String type, String ext) {
    this.id = id;
    this.type = type;
    this.ext = ext;
  }

  public FileType(String type, String ext) {
    this.type = type;
    this.ext = ext;
  }

  @NonNull
  @Override
  public String toString() {
    return this.type;
  }

  public String getType() {
    return type;
  }

  public String getExt() {
    return ext;
  }

}
