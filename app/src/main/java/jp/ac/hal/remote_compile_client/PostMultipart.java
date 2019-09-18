package jp.ac.hal.remote_compile_client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class PostMultipart {
  private final String boundary;
  private static final String LINE_FEED = "\r\n";
  private HttpURLConnection httpConn;
  private String charset;
  private OutputStream outputStream;
  private PrintWriter writer;

  public PostMultipart(String requestURL, String charset) throws IOException {
    this.charset = charset;

    boundary = "===" + System.currentTimeMillis() + "===";
    URL url = new URL(requestURL);
    this.httpConn = (HttpURLConnection) url.openConnection();
    this.httpConn.setUseCaches(false);
    this.httpConn.setDoOutput(true);
    this.httpConn.setDoInput(true);
    this.httpConn.setReadTimeout(10000);
    this.httpConn.setConnectTimeout(20000);
    this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
    outputStream = this.httpConn.getOutputStream();
    writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
  }

  // フォームフィールド追加
  public void addField(String name, String value) {
    writer.append("--").append(boundary).append(LINE_FEED);
    writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
            .append(LINE_FEED);
    writer.append("Content-Type: text/plain; charset=").append(charset).append(
            LINE_FEED);
    writer.append(LINE_FEED);
    writer.append(value).append(LINE_FEED);
    writer.flush();
  }

  // ファイル追加
  public void addFile(String name, File uploadFile)
          throws IOException {
    String fileName = uploadFile.getName();
    writer.append("--").append(boundary).append(LINE_FEED);
    writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED);
//    writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
    writer.append("Content-Type: application/gzip").append(LINE_FEED);
//    writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
    writer.append("Content-Encoding: gzip").append(LINE_FEED);
    writer.append(LINE_FEED);
    writer.flush();

    FileInputStream inputStream = new FileInputStream(uploadFile);
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    outputStream.flush();
    inputStream.close();
    writer.append(LINE_FEED);
    writer.flush();
  }

  // ヘッダー追加
  public void addHeader(String name, String value) {
    writer.append(name).append(": ").append(value).append(LINE_FEED);
    writer.flush();
  }

  // 実行
  public List<String> post() throws IOException {
    List<String> response = new ArrayList<>();
    writer.append(LINE_FEED).flush();
    writer.append("--").append(boundary).append("--").append(LINE_FEED);
    writer.close();

    // checks server's status code first
    int status = httpConn.getResponseCode();
    if (status == HttpURLConnection.HTTP_OK) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(
              httpConn.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        response.add(line);
      }
      reader.close();
      httpConn.disconnect();
    } else {
      throw new IOException("Send Fail: " + status);
    }
    return response;
  }
}
