package jp.ac.hal.remote_compile_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import jp.ac.hal.webview_ace.CompileActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AsyncCompileRequest extends AsyncTask<Void, Void, String> {

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
  protected String doInBackground(Void... voids) {
//    List<String> response = null;
    String response = "";
    try {
//      this.postMultipart = new PostMultipart(this.requestURL, this.charset);
//      // set data here
//      this.postMultipart.addField("fileType", this.fileType);
////      this.postMultipart.addFile("file", this.uploadFile.getGzFile());
//      this.postMultipart.addField("content", this.content);
//
//      response = this.postMultipart.post();
      response = this.postRawWithOkhttp(fileType, content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @Override
  protected void onPostExecute(String response) {
    super.onPostExecute(response);
    // do something after getting response
    Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
  }

  private String postGzWithOkhttp(String fileType, UploadFile uploadFile) throws Exception {
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/gzip");
    @SuppressWarnings("deprecation") RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("dataType", "archive")
        .addFormDataPart("fileType", fileType)
        .addFormDataPart("file", "uploadedFile", RequestBody.create(mediaType, uploadFile.getGzFile()))
        .build();
    Request request = new Request.Builder()
        .url(CompileActivity.SERVER_COMPILE_URL)
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      return Objects.requireNonNull(response.body()).string();
    }

  }

  private String postRawWithOkhttp(String fileType, String content) throws Exception {
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("dataType", "rawCode")
        .addFormDataPart("fileType", fileType)
        .addFormDataPart("content", content)
        .build();
    Request request = new Request.Builder()
        .url(CompileActivity.SERVER_COMPILE_URL)
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      return Objects.requireNonNull(response.body()).string();
    }
  }
}

