package jp.ac.hal.webview_ace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SourceFile {

  private File sourceFile;
  private SourceDir sourceDir;
  private String content;
  private String fileType;

  public SourceFile(SourceDir sourceDir, String fileType, String content) {
    this.setSourceDir(sourceDir);
    this.setContent(content);
    this.setFileType(fileType);
    this.generateSourceFile();
  }

  public SourceFile(String path) {
    this.sourceFile = new File(path);
  }

  public String getFileType() {
    int dotIndex = this.sourceFile.getName().indexOf(".");
    return this.sourceFile.getName().substring(dotIndex);
  }

  public String readSource() {
    StringBuilder str = new StringBuilder();
    String tmp;
    try (
        FileInputStream inputStream = new FileInputStream(this.sourceFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ){
      while ((tmp = br.readLine()) != null) {
        str.append(tmp).append("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return str.toString();
  }

  private void setFileType(String fileType) {
    this.fileType = fileType;
  }

  private void setSourceDir(SourceDir sourceDir) {
    this.sourceDir = sourceDir;
  }

  public void setContent(String content) {
    this.content = content;
  }

  private void generateSourceFile() {
    Date d = new Date();
    long unixTime = d.getTime();
    String fileName = unixTime + "." + this.fileType;
    this.sourceFile = new File(this.sourceDir.getPath(), fileName);
  }

  public void saveFile() {
    try (
        FileOutputStream fileOutputStream = new FileOutputStream(this.sourceFile, false);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(outputStreamWriter)
    ) {
      bw.write(this.content);
      bw.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
