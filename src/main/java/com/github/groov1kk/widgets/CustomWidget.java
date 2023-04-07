package com.github.groov1kk.widgets;

import com.github.groov1kk.utils.actions.SwipeAction;
import io.appium.java_client.pagefactory.Widget;
import java.time.Duration;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

/** Extension of the default Appium {@link Widget} that enhances it with addition methods. */
public abstract class CustomWidget extends Widget {

  protected CustomWidget(WebElement element) {
    super(element);
  }

  /**
   * Checks, whether the widget is displayed.
   *
   * @return is widget displayed
   */
  public boolean isDisplayed() {
    return getWrappedElement().isDisplayed();
  }

  /**
   * Checks, whether the widget is enabled.
   *
   * @return is widget enabled
   */
  public boolean isEnabled() {
    return getWrappedElement().isEnabled();
  }

  /** Clicks on the giving widget. */
  public void click() {
    getWrappedElement().click();
  }

  /**
   * Returns the text from the giving widget.
   *
   * @return widget text
   */
  public String getText() {
    return getWrappedElement().getText();
  }

  /**
   * Returns widget's current location on the screen. Result would be represented as a pair of (x,
   * y) coordinates.
   *
   * @return widget's location on the screen
   * @see Point
   */
  public Point getLocation() {
    return getWrappedElement().getLocation();
  }

  /**
   * Returns the value of the "clickable" attribute, if it presents. Otherwise, returns {@code
   * false}.
   *
   * @return is the widget clickable
   */
  public boolean clickable() {
    return getBooleanAttribute("clickable");
  }

  /**
   * Returns the value of the "checkable" attribute, if it presents. Otherwise, returns {@code
   * false}.
   *
   * @return is the widget checkable
   */
  public boolean checkable() {
    return getBooleanAttribute("checkable");
  }

  /**
   * Swipes the widget on the given X- and Y- axes offsets from its location.
   *
   * @param xOffset X-axis offset for the swipe
   * @param yOffset Y-axis offset for the swipe
   */
  public void swipe(int xOffset, int yOffset) {
    doSwipe(getLocation().moveBy(xOffset, yOffset));
  }

  /**
   * Swipes the widget on a specific offset from its location.
   *
   * @param offset X and Y offset to swipe the widget
   */
  public void swipe(Point offset) {
    doSwipe(getLocation().moveBy(offset.x, offset.y));
  }

  private void doSwipe(Point offset) {
    new SwipeAction(getWrappedDriver())
        .withSource(getLocation())
        .withOffset(offset)
        .withDuration(Duration.ofMillis(200))
        .perform();
  }

  protected boolean getBooleanAttribute(String attrName) {
    return Boolean.parseBoolean(getWrappedElement().getAttribute(attrName));
  }
}
