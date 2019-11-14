package jp.ac.hal.webview_ace;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.regex.Pattern;


public class AceEditorView extends WebView {

  private static final String ACE_INDEX_PATH = "file:///android_asset/index.html";
  public static final String LINE_SEPARATOR = "\n";
  private String content;

  public AceEditorView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setDefaultParams();
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void setDefaultParams() {
    this.loadUrl(ACE_INDEX_PATH);
    this.setWebChromeClient(new WebChromeClient());
    this.getSettings().setJavaScriptEnabled(true);
    this.getSettings().setAllowUniversalAccessFromFileURLs(true);
    this.getSettings().setAllowFileAccessFromFileURLs(true);
    this.getSettings().setLoadWithOverviewMode(true);
    this.getSettings().setUseWideViewPort(true);
  }

  public void setFileType(final String fileType) {
    loadUrl("javascript:setFileType('" + fileType + "');");
  }

  public void fetchContent() {
    this.evaluateJavascript("javascript:fetchContent();", new ValueCallback<String>() {
      @Override
      public void onReceiveValue(String value) {
        content = value;
      }
    });
  }

  public void setContent(String content) {
    loadUrl("javascript:setText('" + content + "');");
  }

  public String getContent() {
    return this.cleanContent();
  }

  private String cleanContent() {
    int length = this.content.length();
    String tmp = this.content.substring(1, length - 1);
    // \\n -> \n , \n -> line_separator
    tmp = this.cleanNewLine(tmp);
    // \" -> ", \' -> '
    tmp = this.cleanQuote(tmp);
    return tmp;
  }

  private String cleanQuote(String tmp) {
    String regDoubleQuote = Pattern.quote("\\\"");
    String regSingleQuote = Pattern.quote("\\\'");
    return tmp.replaceAll(regDoubleQuote, "\"").replaceAll(regSingleQuote, "\'");
  }

  private String cleanNewLine(String tmp) {
    String reg = Pattern.quote("\\\\n");
    tmp = tmp.replaceAll(reg, "\\\\N");
    // \n -> line_separator
    reg = Pattern.quote("\\n");
    tmp = tmp.replaceAll(reg, LINE_SEPARATOR);
    // \N -> \n
    reg = Pattern.quote("\\N");
    return tmp.replaceAll(reg, "\\\\n");
  }

}
