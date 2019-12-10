package jp.ac.hal.webview_ace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  AceEditorView aceView;
  String fileTypeName;
  String filePath;

  public static final int
      REQUEST_PERMISSION_CODE = 1212,
      OPEN_REQUEST_CODE = 1,
      COMPILE_REQUEST_CODE = 2,
      SAVE_FILE_CODE = 3;


  private void requestStoragePermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      Toast.makeText(this, "許可されないとファイルを保存できません", Toast.LENGTH_SHORT).show();
    }
    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode != REQUEST_PERMISSION_CODE) {
      return;
    }
    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      Toast.makeText(this, "権限が許可されました。", Toast.LENGTH_SHORT).show();
    } else {
      // 拒否された時の対応
      Toast.makeText(this, "ファイルの保存ができません。", Toast.LENGTH_SHORT).show();
    }
  }

  public boolean havePermission() {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (!this.havePermission()) {
      requestStoragePermission();
    }

    final Button saveRunBtn = findViewById(R.id.save_run_button);
    final Button fileBtn = findViewById(R.id.file_btn);
    fileBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // ファイル形式の選択 or 既存のファイルを開く
        Intent openIntent = new Intent(MainActivity.this, OpenActivity.class);
        startActivityForResult(openIntent, OPEN_REQUEST_CODE);
      }
    });


    this.aceView = findViewById(R.id.aceEditorWv);

    this.aceView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        saveRunBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (fileTypeName != null && !fileTypeName.isEmpty() && !fileTypeName.equals("ERROR")) {
              aceView.fetchContent();
              // jsが非同期に実行されるため, 若干遅らせる
              new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  Intent compileIntent = new Intent(MainActivity.this, CompileActivity.class);
                  compileIntent.putExtra("content", aceView.getContent());
                  compileIntent.putExtra("fileType", fileTypeName);
                  startActivityForResult(compileIntent, COMPILE_REQUEST_CODE);
                }
              }, 500);
            }
          }
        });
      }
    });


  }

  @SuppressLint("SetTextI18n")
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case OPEN_REQUEST_CODE:
        if (resultCode == RESULT_OK) {
          this.fileTypeName = data.getStringExtra("FILE_TYPE_NAME");
          int openType = data.getIntExtra("OPEN_TYPE", OpenActivity.OPEN_TYPE_ERR);
          if (openType == OpenActivity.OPEN_TYPE_OPEN) {
            this.filePath = data.getStringExtra("OPEN_FILE");
            SourceFile sourceFile = new SourceFile(this.filePath);
            this.fileTypeName = sourceFile.getFileType();
            this.aceView.setContent(sourceFile.readSource());
          } else {
            this.filePath = "";
          }
        } else {
          this.fileTypeName = "ERROR";
        }
        break;
      case COMPILE_REQUEST_CODE:
        break;
      default:
        break;
    }
    if (!this.fileTypeName.equals("ERROR")) {
      this.aceView.setFileType(this.fileTypeName);
    }
  }

}
