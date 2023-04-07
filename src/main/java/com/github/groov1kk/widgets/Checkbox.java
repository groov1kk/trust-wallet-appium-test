package com.github.groov1kk.widgets;

import org.openqa.selenium.WebElement;

/** Representation of android.widget.CheckBox. */
public class Checkbox extends CustomWidget {

  protected Checkbox(WebElement element) {
    super(element);
  }

  /**
   * Check, whether the checkbox is checked.
   *
   * @return is the checkbox checked
   */
  public boolean isChecked() {
    return getBooleanAttribute("checked");
  }

  /** If the checkbox is unchecked - this method will check it. */
  public void check() {
    if (!isChecked()) {
      click();
    }
  }
}
