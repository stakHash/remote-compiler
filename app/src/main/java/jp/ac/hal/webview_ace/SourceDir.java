package jp.ac.hal.webview_ace;

import android.os.Environment;

import java.io.File;

public class SourceDir extends File {

  private static final String SOURCE_DIR_NAME = "source";

  public SourceDir() {
    super(Environment.getExternalStorageDirectory().getPath(), SOURCE_DIR_NAME);
    this.gen();
  }

  private void gen() {
    if (!this.exists()) {
      this.mkdir();
    }
  }

}
