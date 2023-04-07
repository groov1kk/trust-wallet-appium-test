package com.github.groov1kk.core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.SlowLoadableComponent;

/**
 * This class consists of static utility methods for operating on page objects, or checking certain
 * conditions before operation.
 *
 * <p>The idea of this class is creating analog of {@link java.util.Objects} utility class. Mostly
 * it was designed to create validations in page object constructors and methods.
 *
 * <p>Also, the methods of this class in cases of unsatisfied conditions throw instances of {@link
 * Error}, which provides to use them as condition in methods {@link
 * SlowLoadableComponent#isLoaded()} or {@link SlowLoadableComponent#isError()}.
 */
public final class PageObjects {

  private PageObjects() {}

  /**
   * Checks that it is possible to find an element using the given locator and the found element is
   * visible.
   *
   * @param page page object on which the given locator has to be verified
   * @param locator locator which is using to find the desired element
   * @return web element if it is possible to find it, and it is visible
   * @throws Error if it is not possible to find an element using the given locator or if the
   *     element is not visible
   */
  public static WebElement requireVisibleElement(BaseScreen page, By locator) {
    return requireVisibleElement(
        page, locator, String.format("Element with locator [%s] is not visible", locator));
  }

  /**
   * Checks that it is possible to find an element using the given locator and the found element is
   * visible.
   *
   * @param page page object on which the given locator has to be verified
   * @param locator locator which is using to find the desired element
   * @param message detail message to be used in the event that a {@link Error} is thrown
   * @return web element if it is possible to find it, and it is visible
   * @throws Error if it is not possible to find an element using the given locator or if the
   *     element is not visible
   */
  public static WebElement requireVisibleElement(BaseScreen page, By locator, String message) {
    WebElement element;
    try {
      element = page.findElement(locator);
    } catch (StaleElementReferenceException | NoSuchElementException e) {
      throw new Error(message, e);
    }

    if (!element.isDisplayed()) {
      throw new Error(message);
    }
    return element;
  }
}
