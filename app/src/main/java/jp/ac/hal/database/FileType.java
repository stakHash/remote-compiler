package jp.ac.hal.database;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class FileType implements Serializable {

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
    return this.type;
  }

  public String getExt() {
    return this.ext;
  }

  public int getId() {
    return this.id;
  }
}
