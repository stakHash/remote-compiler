package jp.ac.hal.spinner;

interface KeyValueItem{
  String getOptionLabel();
  long getOptionValue();
  int getId();
  void setId(int id);
  String getName();
  void setName(String name);
}

public class SpinnerObject implements KeyValueItem {

  private int id;
  private String name;

  SpinnerObject(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public long getOptionValue() {
    return id;
  }

  @Override
  public String getOptionLabel() {
    return name;
  }
}
