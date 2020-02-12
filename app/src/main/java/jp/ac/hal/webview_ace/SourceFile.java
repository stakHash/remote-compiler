package jp.ac.hal.webview_ace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;

import jp.ac.hal.database.FileType;

public class SourceFile {

  private File sourceFile;
  private SourceDir sourceDir;
  private String content;
  private FileType fileType;
  private String fileName;

  public SourceFile(SourceDir sourceDir, String fileName, FileType fileType, String content) {
    this.setSourceDir(sourceDir);
    this.setContent(content);
    this.setFileType(fileType);
    this.fileName = fileName + "." + fileType.getExt();
    this.generateSourceFile();
  }

  public SourceFile(String path) {
    this.sourceFile = new File(path);
  }


  String getFileExt() {
    int dotIndex = this.sourceFile.getName().indexOf(".");
    return this.sourceFile.getName().substring(dotIndex);
  }

  String readSource() {
    StringBuilder str = new StringBuilder();
    String tmp;
    try (
        FileInputStream inputStream = new FileInputStream(this.sourceFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
    ) {
      while ((tmp = br.readLine()) != null) {
        str.append(tmp).append("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return str.toString();
  }

  private void setFileType(FileType fileType) {
    this.fileType = fileType;
  }

  private void setSourceDir(SourceDir sourceDir) {
    this.sourceDir = sourceDir;
  }

  public void setContent(String content) {
    this.content = content;
  }

  private void generateSourceFile() {
    this.sourceFile = new File(this.sourceDir.getPath(), this.fileName);
  }

  void overwriteFile() {
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

  void saveFile() throws FileAlreadyExistsException {
    if (this.sourceFile.exists()) {
      throw new FileAlreadyExistsException(this.fileName + " は既に存在しています。");
    }
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
