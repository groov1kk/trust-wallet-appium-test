package com.github.groov1kk.widgets;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.Widget;
import org.openqa.selenium.WebElement;

/** Extension of the default Appium {@link Widget} that enhances it with addition methods. */
public class ExtendedWidget extends Widget {

  protected ExtendedWidget(WebElement element) {
    super(element);
  }

  public boolean isDisplayed() {
    return getWrappedElement().isDisplayed();
  }

//  @Override
//  public AppiumDriver getWrappedDriver() {
//    return (AppiumDriver) super.getWrappedDriver();
//  }
}
