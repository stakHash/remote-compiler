package jp.ac.hal.webview_ace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import jp.ac.hal.remote_compile_client.AsyncCompileRequest;
import jp.ac.hal.remote_compile_client.CacheDir;

public class CompileActivity extends AppCompatActivity {

  public static final String
      CHARSET = "UTF-8",
      SERVER_PROTOCOL = "http://",
      SERVER_IP_ADDRESS = SERVER_PROTOCOL + "10.0.2.2",
      SERVER_PORT = ":8888",
      SERVER_PING_URL = SERVER_IP_ADDRESS + SERVER_PORT + "/ping",
      SERVER_COMPILE_URL = SERVER_IP_ADDRESS + SERVER_PORT + "/exec";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compile);

    Intent intent = getIntent();
    final String content = intent.getStringExtra("content");
    final String fileType = intent.getStringExtra("fileType");


    Button saveBtn = findViewById(R.id.save_button);
    Button runBtn = findViewById(R.id.run_button);
    final TextView execResultTv = findViewById(R.id.exec_result);

    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SourceDir sourceDir = new SourceDir();
        SourceFile sourceFile = new SourceFile(sourceDir, fileType, content);
        sourceFile.saveFile();
        Toast.makeText(CompileActivity.this, "saved source file", Toast.LENGTH_SHORT).show();
      }
    });

    runBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        CacheDir cacheDir = new CacheDir(CompileActivity.this);
//        UploadFile uploadFile = new UploadFile(fileType, cacheDir);
//        uploadFile.doZip(content);

//        new AsyncCompileRequest(CompileActivity.this, SERVER_COMPILE_URL, uploadFile, fileType, CHARSET).execute();
        AsyncCompileRequest ac = new AsyncCompileRequest(CompileActivity.this, SERVER_COMPILE_URL, content, fileType, CHARSET);
        ac.setResultTextView(execResultTv);
        ac.execute();

        // debug (ping/pong)
//        new AsyncCompileRequest(CompileActivity.this, SERVER_PING_URL, CHARSET).execute();
      }
    });

    TextView previewTV = findViewById(R.id.preview);

    previewTV.setText(content);
  }

}
