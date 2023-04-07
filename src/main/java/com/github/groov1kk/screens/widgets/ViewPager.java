package com.github.groov1kk.screens.widgets;

import com.github.groov1kk.widgets.CustomWidget;
import com.github.groov1kk.widgets.Image;
import com.github.groov1kk.widgets.Text;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ViewPager extends CustomWidget {

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/img")
  private Image image;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/title")
  private Text title;

  protected ViewPager(WebElement element) {
    super(element);
  }

  public String getTitle() {
    return title.getText();
  }

  public boolean hasImage() {
    return image.isDisplayed();
  }

  public void swipeLeft() {
    image.swipe(-650, 0);
  }

  public void swipeRight() {
    image.swipe(650, 0);
  }
}
