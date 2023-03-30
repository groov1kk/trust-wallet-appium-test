package com.github.groov1kk.utils;

import org.openqa.selenium.WebElement;

public final class WebElementUtils {

  private WebElementUtils() {}

  /**
   * Check, whether the element is checked.
   *
   * @param element element to verify
   * @return is element checked
   */
  public static boolean isChecked(WebElement element) {
    return Boolean.parseBoolean(element.getAttribute("checked"));
  }

  /**
   * If the given element is checkbox, and it is unchecked - this method will check it.
   *
   * @param element element to check
   */
  public static void check(WebElement element) {
    if (!Boolean.parseBoolean(element.getAttribute("checkable"))) {
      return;
    }

    if (!isChecked(element)) {
      element.click();
    }
  }
}
