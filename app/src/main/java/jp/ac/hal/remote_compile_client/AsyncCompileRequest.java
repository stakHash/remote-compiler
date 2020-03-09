package jp.ac.hal.remote_compile_client;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AsyncCompileRequest extends AsyncTask<Void, Void, String> {

  private static final String
      CHARSET = "UTF-8",
      SERVER_PROTOCOL = "http://",
      SERVER_IP_ADDRESS = SERVER_PROTOCOL + "10.0.2.2",
//      SERVER_IP_ADDRESS = "192.168.100.2",
//    SERVER_IP_ADDRESS = "10.192.109.1",
    SERVER_PORT = ":8887",
      SERVER_COMPILE_URL = SERVER_PROTOCOL + SERVER_IP_ADDRESS + SERVER_PORT + "/exec";
  //      SERVER_PING_URL = SERVER_IP_ADDRESS + SERVER_PORT + "/ping",

  private static final MediaType MEDIA_TYPE_GZIP = MediaType.parse("application/gzip");

  private Context context;
  private UploadFile uploadFile;
  private String fileType;
  private PostMultipart postMultipart;
  private String content;

  public AsyncCompileRequest(Context context, String content, String fileType) {
    this.context = context;
    this.content = content;
    this.fileType = fileType;
  }

  public AsyncCompileRequest(Context context, UploadFile uploadFile, String fileType) {
    this.context = context;
    this.uploadFile = uploadFile;
    this.fileType = fileType;
  }

//  // for ping/pong
//  public AsyncCompileRequest(Activity activity) {
//    this.activity = activity;
//  }

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
      response = this.postRawWithOkHttp(fileType, content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    Toast.makeText(this.context, "サーバの応答を待っています。", Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onPostExecute(String response) {
    super.onPostExecute(response);
    // do something after getting response
//    this.resultTv.setText(response);
    new AlertDialog.Builder(this.context)
        .setTitle("実行結果")
        .setMessage(response)
        .setPositiveButton("OK", null)
        .show();
  }

  private String postGzWithOkHttp(String fileType, UploadFile uploadFile) throws Exception {
    OkHttpClient client = new OkHttpClient();
    @SuppressWarnings("deprecation") RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("dataType", "archive")
        .addFormDataPart("fileType", fileType)
        .addFormDataPart("file", "uploadedFile", RequestBody.create(MEDIA_TYPE_GZIP, uploadFile.getGzFile()))
        .build();
    Request request = new Request.Builder()
        .url(SERVER_COMPILE_URL)
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      return Objects.requireNonNull(response.body()).string();
    }

  }

  private String postRawWithOkHttp(String fileType, String content) throws Exception {
    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("dataType", "rawCode")
        .addFormDataPart("fileType", fileType)
        .addFormDataPart("content", content)
        .build();
    Request request = new Request.Builder()
        .url(SERVER_COMPILE_URL)
        .post(requestBody)
        .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

      return Objects.requireNonNull(response.body()).string();
    }
  }
}

