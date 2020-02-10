package jp.ac.hal.Token;

import android.content.SharedPreferences;

public class TokenManager {

  private SharedPreferences sharedPreferences;
  private static final String GITHUB_TOKEN_SP_NAME = "GITHUB_TOKEN";
  private static final String GITHUB_TOKEN_SP_DEFAULT_VALUE = "TOKEN_NOT_SET";

  public TokenManager(SharedPreferences sp) {
    this.sharedPreferences = sp;
  }

  public void SaveToken(String token) {
    this.sharedPreferences
        .edit()
        .putString(GITHUB_TOKEN_SP_NAME, token)
        .apply();
  }

  public String GetToken() {
    String tmp = this.sharedPreferences.getString(GITHUB_TOKEN_SP_NAME, GITHUB_TOKEN_SP_DEFAULT_VALUE);
    if (tmp != null && !tmp.equals(GITHUB_TOKEN_SP_DEFAULT_VALUE)) {
      return tmp;
    } else {
      return "";
    }
  }

//  private String encode(String raw) {
//  }

//  private String decode(String token) {
//  }

}
