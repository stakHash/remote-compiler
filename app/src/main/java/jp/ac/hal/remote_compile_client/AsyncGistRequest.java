package jp.ac.hal.remote_compile_client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

import java.io.IOException;
import java.util.Collections;

public class AsyncGistRequest extends AsyncTask<Void, Void, Gist> {

  private Context parent;
  private String content;
  private String fileName;
  private String token;

  public AsyncGistRequest(Context parent, String content, String fileName, String token) {
    this.parent = parent;
    this.content = content;
    this.fileName = fileName;
    this.token = token;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    Toast.makeText(this.parent, "サーバの応答を待っています...", Toast.LENGTH_LONG).show();
  }

  @Override
  protected Gist doInBackground(Void... voids) {
    GistFile gistFile = new GistFile();
    Gist gist = new Gist();
    gistFile.setContent(this.content);
    gist.setDescription("upload from AnyEditor");
    gist.setFiles(Collections.singletonMap(this.fileName, gistFile));
    GistService service = new GistService();
    service.getClient().setOAuth2Token(this.token);
    try {
      return service.createGist(gist);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  protected void onPostExecute(Gist gist) {
    super.onPostExecute(gist);
    String msg;
    AlertDialog.Builder builder = new AlertDialog.Builder(this.parent)
        .setTitle("アップロード")
        .setPositiveButton("OK", null);
    if (gist != null) {
      final String url = gist.getHtmlUrl();
      builder.setNeutralButton("ブラウザで開く", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          parent.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
      });
      msg = "アップロードに成功しました。\nURL: " + url;
    } else {
      msg = "アップロードに失敗しました。";
    }
    builder.setMessage(msg);
    builder.show();
  }
}
