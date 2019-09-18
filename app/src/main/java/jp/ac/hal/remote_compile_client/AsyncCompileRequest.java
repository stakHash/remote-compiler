package jp.ac.hal.remote_compile_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;


public class AsyncCompileRequest extends AsyncTask<Void, Void, List<String>> {

  private String requestURL;
  private String charset;
  private UploadFile uploadFile;
  private String fileType;
  private Activity activity;
  private PostMultipart postMultipart;
  private String content;

  public AsyncCompileRequest(Activity activity, String requestURL, String content, String fileType, String charset) {
    this.requestURL = requestURL;
    this.charset = charset;
    this.activity = activity;
    this.content = content;
    this.fileType = fileType;
  }

  public AsyncCompileRequest(Activity activity, String requestURL, UploadFile uploadFile, String fileType, String charset) {
    this.requestURL = requestURL;
    this.charset = charset;
    this.activity = activity;
    this.uploadFile = uploadFile;
    this.fileType = fileType;
  }

  // for ping/pong
  public AsyncCompileRequest(Activity activity, String requestURL, String charset) {
    this.requestURL = requestURL;
    this.charset = charset;
    this.activity = activity;
  }

  @Override
  protected List<String> doInBackground(Void... voids) {
    List<String> response = null;
    try {
      this.postMultipart = new PostMultipart(this.requestURL, this.charset);
      // set data here
      this.postMultipart.addField("fileType", this.fileType);
//      this.postMultipart.addFile("file", this.uploadFile.getGzFile());
      this.postMultipart.addField("content", this.content);

      response = this.postMultipart.post();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return response;
  }

  @Override
  protected void onPostExecute(List<String> response) {
    super.onPostExecute(response);
    // do something after getting response
    String responseJson = response.get(0);
    Toast.makeText(activity, responseJson, Toast.LENGTH_SHORT).show();
  }
}

