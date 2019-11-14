package jp.ac.hal.remote_compile_client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public class UploadFile {

  private CacheDir cacheDir;
  private String fileType;
  private File gzFile;

  public UploadFile(String fileType, CacheDir cache) {
    this.setFileType(fileType);
    this.setCacheDir(cache);
  }

  private void setFileType(String fileType) {
    this.fileType = fileType;
  }

  private void setCacheDir(CacheDir cache) {
    this.cacheDir = cache;
  }

  File getGzFile() {
    return this.gzFile;
  }

  public void doZip(String content) {
    this.genGzipFile();
    try (
        FileOutputStream outputStream = new FileOutputStream(gzFile);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)
    ) {
      gzipOutputStream.write(content.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void doZip(File inputFile) {
    this.genGzipFile();
    StringBuilder str = new StringBuilder();
    String tmp;
    try (
        FileInputStream inputStream = new FileInputStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        FileOutputStream outputStream = new FileOutputStream(gzFile);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)
    ) {
      while ((tmp = br.readLine()) != null) {
        str.append(tmp).append("\n");
      }
      gzipOutputStream.write(str.toString().getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void genGzipFile() {
    this.gzFile = new File(this.cacheDir.getPath(), this.generateGzipFileName());
  }

  private String generateGzipFileName() {
    Date d = new Date();
    long unixTime = d.getTime();
    return this.fileType + unixTime + ".gz";
  }

}
