package com.github.groov1kk.widgets;

import com.google.common.base.Preconditions;
import org.openqa.selenium.WebElement;

/** Representation of android.widget.Button. */
public class Button extends CustomWidget {

  protected Button(WebElement element) {
    super(element);
    Preconditions.checkState(clickable(), String.format("Element %s is not clickable", element));
  }
}
