package jp.ac.hal.webview_ace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.view.FilePickerDialog;

import jp.ac.hal.database.FileType;
import jp.ac.hal.database.FileTypeTable;
import jp.ac.hal.database.SQLiteOpener;
import jp.ac.hal.file_picker.FilePicker;

public class OpenActivity extends AppCompatActivity {

  public static final int
      OPEN_TYPE_ERR = -1,
      OPEN_TYPE_NEW = 0,
      OPEN_TYPE_OPEN = 1;
  String selectedPath;
  private EditText fileEt;
  private Spinner sp;
  private FileTypeTable table;
  private FileType spinnerFileType;
  private FileType openFileType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_open);

    Button nextBt = findViewById(R.id.button2);
    final Button selectBt = findViewById(R.id.button4);
    this.fileEt = findViewById(R.id.editText2);

    SQLiteOpener opener = new SQLiteOpener(this);
    this.table = new FileTypeTable(opener.getReadableDatabase());
    this.sp = findViewById(R.id.spinner);
    ArrayAdapter<FileType> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, this.table.getAllTypes());
    this.sp.setAdapter(adapter);

    final Intent intent = new Intent();
    final RadioGroup radioGroup = findViewById(R.id.radioGroupOpen);

    this.sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        spinnerFileType = (FileType) sp.getSelectedItem();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.radioNewFile:
            sp.setEnabled(true);
            fileEt.setEnabled(false);
            selectBt.setEnabled(false);
            break;
          case R.id.radioOpenFile:
            sp.setEnabled(false);
            fileEt.setEnabled(true);
            selectBt.setEnabled(true);
            break;
        }
      }
    });
    radioGroup.check(R.id.radioNewFile);

    final FilePickerDialog fpDialog = new FilePickerDialog(OpenActivity.this, new FilePicker(FilePicker.MODE_SINGLE_FILE));
    fpDialog.setTitle("Open");

    selectBt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        fpDialog.show();
      }
    });

    fpDialog.setDialogSelectionListener(new DialogSelectionListener() {
      @Override
      public void onSelectedFilePaths(String[] files) {
        selectedPath = files[0];
        fileEt.setText(selectedPath);
      }
    });

    nextBt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (radioGroup.getCheckedRadioButtonId()) {
          case R.id.radioNewFile:
            intent.putExtra("OPEN_TYPE", OPEN_TYPE_NEW);
            intent.putExtra("FILE_TYPE", spinnerFileType);
            break;
          case R.id.radioOpenFile:
            OpenActivity.this.setOpenFileType();
            intent.putExtra("OPEN_TYPE", OPEN_TYPE_OPEN);
            intent.putExtra("FILE_TYPE", openFileType);
            intent.putExtra("OPEN_FILE", selectedPath);
            intent.putExtra("OPEN_FILE_NAME", getFileName());
            break;
          default:
            break;
        }
        OpenActivity.this.setResult(RESULT_OK, intent);
        OpenActivity.this.finish();
      }
    });
  }

  private String getFileName() {
    int lastSlash = this.selectedPath.lastIndexOf("/");
    int point = this.selectedPath.lastIndexOf(".");
    return this.selectedPath.substring(lastSlash + 1, point);
  }

  private void setOpenFileType() {
    if (this.selectedPath == null || this.selectedPath.equals("")) {
      this.openFileType = null;
    } else {
      int point = this.selectedPath.lastIndexOf(".");
      if (point != -1) {
        this.openFileType = this.table.getTypeFromExt(this.selectedPath.substring((point + 1)));
      } else {
        this.openFileType = null;
      }
    }
  }
}
