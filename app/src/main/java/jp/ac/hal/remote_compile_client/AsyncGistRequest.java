package jp.ac.hal.remote_compile_client;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.service.GistService;

import java.io.IOException;
import java.util.Collections;

public class AsyncGistRequest extends AsyncTask<Void, Void, Boolean> {

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
  protected Boolean doInBackground(Void... voids) {
    GistFile gistFile = new GistFile();
    Gist gist = new Gist();
    gistFile.setContent(this.content);
    gist.setDescription("upload from AnyEditor");
    gist.setFiles(Collections.singletonMap(this.fileName, gistFile));
    GistService service = new GistService();
    service.getClient().setOAuth2Token(this.token);
    try {
      service.createGist(gist);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  protected void onPostExecute(Boolean b) {
    super.onPostExecute(b);
    String msg;
    if (b) {
      msg = "アップロードに成功しました。";
    } else {
      msg = "アップロードに失敗しました。";
    }
    Toast.makeText(this.parent, msg, Toast.LENGTH_SHORT).show();
  }
}
