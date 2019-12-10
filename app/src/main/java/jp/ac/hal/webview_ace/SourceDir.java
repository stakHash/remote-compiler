package jp.ac.hal.webview_ace;

import android.os.Environment;

import java.io.File;

public class SourceDir extends File {

  private static final String DEFAULT_SOURCE_DIR_NAME = "source";

  public SourceDir() {
    super(Environment.getExternalStorageDirectory().getPath(), DEFAULT_SOURCE_DIR_NAME);
    this.gen();
  }

  public SourceDir(String path) {
    super(Environment.getExternalStorageDirectory().getPath(), DEFAULT_SOURCE_DIR_NAME + "/" + path);
    this.gen();
  }

  private void gen() {
    if (!this.exists()) {
      this.mkdir();
    }
  }

}
