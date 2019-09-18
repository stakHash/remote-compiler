package jp.ac.hal.spinner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;

public class SpinnerHelper {

  private static ArrayList<SpinnerObject> getList(Resources res, int arrayId) {
    TypedArray spinner_data = res.obtainTypedArray(arrayId);
    ArrayList<SpinnerObject> list = new ArrayList<>();
    for (int i = 0; i < spinner_data.length(); ++i) {
      int id = spinner_data.getResourceId(i, -1);
      if (id > -1) {
        String[] item = res.getStringArray(id);
        list.add(new SpinnerObject(Integer.valueOf(item[0]), item[1]));
      }
    }
    spinner_data.recycle();
    return list;
  }

  public static KeyValueAdapter getKeyValueAdapter(Context cxt, Resources res, int arrayId) {
    ArrayList<SpinnerObject> list = getList(res, arrayId);
    KeyValueAdapter adapter = new KeyValueAdapter(cxt, android.R.layout.simple_spinner_item, list);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    return adapter;
  }

}
