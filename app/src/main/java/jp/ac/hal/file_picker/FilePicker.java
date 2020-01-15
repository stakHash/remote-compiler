package jp.ac.hal.file_picker;

import android.os.Environment;

import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;

import java.io.File;

import jp.ac.hal.webview_ace.SourceDir;

public class FilePicker extends DialogProperties {

  public static final int MODE_SINGLE_DIR = 0;
  public static final int MODE_SINGLE_FILE = 1;

  public FilePicker(int mode) {
    setMode(mode);
  }

  private void setMode(int mode) {
    switch (mode) {
      case MODE_SINGLE_DIR:
        setModeDir();
        break;
      case MODE_SINGLE_FILE:
        setModeFile();
        break;
      default:
        setModeDefault();
        break;
    }
  }

  private void setModeDefault() {
    this.selection_type = DialogConfigs.FILE_AND_DIR_SELECT;
    this.selection_mode = DialogConfigs.SINGLE_MODE;
    SourceDir defaultDir = new SourceDir();
    if (defaultDir.exists()) {
      this.root = defaultDir;
    } else {
      this.root = Environment.getExternalStorageDirectory();
    }
    this.error_dir = new File(DialogConfigs.DEFAULT_DIR);
    this.offset = new File(DialogConfigs.DEFAULT_DIR);
    this.extensions = null;
  }

  private void setModeDir() {
    this.selection_type = DialogConfigs.DIR_SELECT;
    this.selection_mode = DialogConfigs.SINGLE_MODE;
    SourceDir defaultDir = new SourceDir();
    if (defaultDir.exists()) {
      this.root = defaultDir;
    } else {
      this.root = Environment.getExternalStorageDirectory();
    }
    this.error_dir = new File(DialogConfigs.DEFAULT_DIR);
    this.offset = new File(DialogConfigs.DEFAULT_DIR);
    this.extensions = null;
  }

  private void setModeFile() {
    this.selection_type = DialogConfigs.FILE_SELECT;
    this.selection_mode = DialogConfigs.SINGLE_MODE;
    SourceDir defaultDir = new SourceDir();
    if (defaultDir.exists()) {
      this.root = defaultDir;
    } else {
      this.root = Environment.getExternalStorageDirectory();
    }
    this.error_dir = new File(DialogConfigs.DEFAULT_DIR);
    this.offset = new File(DialogConfigs.DEFAULT_DIR);
    this.extensions = null;
  }

}
