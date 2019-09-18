package jp.ac.hal.remote_compile_client;

import android.content.Context;

public class CacheDir {

  private String path;

  public CacheDir(Context context) {
    this.path = context.getCacheDir().getPath();
  }

  public String getPath() {
    return this.path;
  }

}
