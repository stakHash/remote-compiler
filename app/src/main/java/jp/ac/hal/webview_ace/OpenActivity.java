package jp.ac.hal.webview_ace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.view.FilePickerDialog;

import java.util.HashMap;
import java.util.Map;

import jp.ac.hal.file_picker.FilePicker;
import jp.ac.hal.spinner.KeyValueAdapter;
import jp.ac.hal.spinner.SpinnerHelper;

public class OpenActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  private String selectedFileTypeName;

  public Map returnMap;
  private EditText fileEt;
  String selectedPath;
  public static final int
      OPEN_TYPE_ERR = -1,
      OPEN_TYPE_NEW = 0,
      OPEN_TYPE_OPEN = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_open);

    this.returnMap = new HashMap<String, String>();

    Button nextBt = findViewById(R.id.button2);
    final Button selectBt = findViewById(R.id.button4);
    this.fileEt = findViewById(R.id.editText2);
    final Spinner sp = findViewById(R.id.spinner);
    final Intent intent = new Intent();
    final RadioGroup radioGroup = findViewById(R.id.radioGroupOpen);

    sp.setOnItemSelectedListener(this);

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
            intent.putExtra("FILE_TYPE_NAME", selectedFileTypeName);
            break;
          case R.id.radioOpenFile:
            intent.putExtra("OPEN_TYPE", OPEN_TYPE_OPEN);
            intent.putExtra("FILE_TYPE_NAME", OpenActivity.this.getSuffix(selectedPath));
            intent.putExtra("OPEN_FILE", selectedPath);
            break;
          default:
            break;
        }
        OpenActivity.this.setResult(RESULT_OK, intent);
        OpenActivity.this.finish();
      }
    });

    KeyValueAdapter adapter = SpinnerHelper.getKeyValueAdapter(this, getResources(), R.array.file_type_data);
    sp.setAdapter(adapter);

  }

  @Override
  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    this.selectedFileTypeName = adapterView.getSelectedItem().toString();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {

  }

  private String getSuffix(String fileName) {
    if (fileName == null) {
      return null;
    }
    int point = fileName.lastIndexOf(".");
    if (point != -1) {
      return fileName.substring(point + 1);
    }
    return null;
  }
}
