package com.github.groov1kk.screens.widgets;

import static java.time.Duration.ofMillis;
import static java.util.Collections.singleton;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;

import com.github.groov1kk.widgets.ExtendedWidget;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public class ViewPager extends ExtendedWidget {

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/img")
  private WebElement image;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/title")
  private WebElement title;

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
    Point source = image.getLocation();
    Point offset = source.moveBy(-650, 0);

    swipe(source, offset);
  }

  public void swipeRight() {
    Point source = image.getLocation();
    Point offset = source.moveBy(650, 0);

    swipe(source, offset);
  }

  private void swipe(Point source, Point offset) {
    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
    Sequence swipe = new Sequence(finger, 1);
    swipe.addAction(finger.createPointerMove(ofMillis(0), viewport(), source));
    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
    swipe.addAction(finger.createPointerMove(ofMillis(200), viewport(), offset));
    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
    ((AndroidDriver) getWrappedDriver()).perform(singleton(swipe));
  }
}
