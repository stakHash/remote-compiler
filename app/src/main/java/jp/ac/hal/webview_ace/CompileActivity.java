package jp.ac.hal.webview_ace;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.FileAlreadyExistsException;

import jp.ac.hal.Token.TokenManager;
import jp.ac.hal.database.FileType;
import jp.ac.hal.remote_compile_client.AsyncCompileRequest;
import jp.ac.hal.remote_compile_client.AsyncGistRequest;
//import jp.ac.hal.remote_compile_client.CacheDir;

public class CompileActivity extends AppCompatActivity {

  private TokenManager tokenManager;
  private static final String GITHUB_TOKEN_URI = "https://github.com/settings/tokens";
//  public static final String
//      CHARSET = "UTF-8",
//      SERVER_PROTOCOL = "http://",
//      SERVER_IP_ADDRESS = SERVER_PROTOCOL + "10.0.2.2",
//      SERVER_PORT = ":8888",
////      SERVER_PING_URL = SERVER_IP_ADDRESS + SERVER_PORT + "/ping",
//      SERVER_COMPILE_URL = SERVER_IP_ADDRESS + SERVER_PORT + "/exec";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_compile);

    Intent intent = getIntent();
    final String content = intent.getStringExtra("content");
    final String fileName = intent.getStringExtra("fileName");
    final FileType fileType = (FileType) intent.getSerializableExtra("fileType");

    TextView extTv = findViewById(R.id.comp_ext);
    final String ext = "." + fileType.getExt();
    extTv.setText(ext);

    this.tokenManager = new TokenManager(getPreferences(MODE_PRIVATE));
    final EditText tokenEt = findViewById(R.id.compile_token_input);
    tokenEt.setText(this.tokenManager.GetToken());

    Button saveBtn = findViewById(R.id.save_button);
    Button runBtn = findViewById(R.id.run_button);
    Button gistBtn = findViewById(R.id.gist_button);
    Button tokenSaveBtn = findViewById(R.id.comp_token_save_button);
    final EditText fileNameEt = findViewById(R.id.comp_file_name);
    fileNameEt.setText(fileName);

    findViewById(R.id.comp_github_link).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Uri uri = Uri.parse(GITHUB_TOKEN_URI);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
      }
    });

    tokenSaveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        tokenManager.SaveToken(tokenEt.getText().toString());
//        Toast.makeText(CompileActivity.this, "トークンを保存しました。", Toast.LENGTH_SHORT).show();
        showDialog(CompileActivity.this, "トークン", "トークンを保存しました。", "OK");
      }
    });

    gistBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String fileName = fileNameEt.getText().toString() + ext;
        AsyncGistRequest gistRequest = new AsyncGistRequest(CompileActivity.this, content, fileName, tokenManager.GetToken());
        gistRequest.execute();
      }
    });

    saveBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SourceDir sourceDir = new SourceDir();
        final SourceFile sourceFile = new SourceFile(sourceDir, fileNameEt.getText().toString(),fileType, content);
        try {
          sourceFile.saveFile();
//          Toast.makeText(CompileActivity.this, "ファイルを保存しました。", Toast.LENGTH_SHORT).show();
          showDialog(CompileActivity.this, "ファイル", "ファイルを保存しました。", "OK");
        } catch (FileAlreadyExistsException e) {
          new AlertDialog.Builder(CompileActivity.this)
              .setTitle("上書き確認")
              .setMessage(e.getMessage() + "\n上書きしますか？")
              .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  sourceFile.overwriteFile();
//                  Toast.makeText(CompileActivity.this, "ファイルを保存しました。", Toast.LENGTH_SHORT).show();
                  showDialog(CompileActivity.this, "ファイル", "ファイルを保存しました。", "OK");
                }
              })
              .setNegativeButton("キャンセル", null)
              .show();
        }
      }
    });

    runBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        CacheDir cacheDir = new CacheDir(CompileActivity.this);
//        UploadFile uploadFile = new UploadFile(fileType, cacheDir);
//        uploadFile.doZip(content);

//        new AsyncCompileRequest(CompileActivity.this, SERVER_COMPILE_URL, uploadFile, fileType, CHARSET).execute();
        AsyncCompileRequest ac = new AsyncCompileRequest(CompileActivity.this, content, fileType.getType());
        ac.execute();

        // debug (ping/pong)
//        new AsyncCompileRequest(CompileActivity.this, SERVER_PING_URL, CHARSET).execute();
      }
    });

    TextView previewTV = findViewById(R.id.preview);

    previewTV.setText(content);
  }

  private void showDialog(Context context, String title, String message, String positiveButton) {
    new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton, null)
        .show();
  }

}
