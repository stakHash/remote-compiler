package jp.ac.hal.spinner;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class KeyValueAdapter extends ArrayAdapter {
  private ArrayList<? extends KeyValueItem> items;
  private Context context;

  KeyValueAdapter(Context context, int resourceId, ArrayList<? extends KeyValueItem> items) {
    super(context, resourceId);
    this.context = context;
    this.items = items;
  }

  public int getCount() {
    return items.size();
  }

  public String getItem(int position) {
    return items.get(position).getOptionLabel();
  }

  public long getItemId(int position) {
    return items.get(position).getOptionValue();
  }

  public int getPosition(long itemId) {
    for (int i = 0; i < items.size(); i++) {
      if (items.get(i).getOptionValue() == itemId) {
        return i;
      }
    }
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView label = new TextView(context);
    label.setTextColor(Color.BLACK);
    label.setText(items.get(position).getOptionLabel());
    return label;
  }
}
