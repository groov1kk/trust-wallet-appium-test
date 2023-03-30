package com.github.groov1kk.core;

import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import com.github.groov1kk.core.annotations.AndroidWaitFor;
import io.appium.java_client.pagefactory.WithTimeout;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;

final class WaitingHelper {

  private WaitingHelper() {}

  public static void waitForScreen(Object screen, WebDriver driver) {
    waitForAndroid(screen.getClass(), driver);
  }

  private static void waitForAndroid(Class<?> clazz, WebDriver driver) {
    if (!clazz.isAnnotationPresent(AndroidWaitFor.class)) {
      return;
    }

    AndroidWaitFor waitFor = clazz.getAnnotation(AndroidWaitFor.class);
    By by = new AndroidWaitForByBuilder(waitFor).buildBy();
    WithTimeout timeout = waitFor.timeout();
    boolean visibility = waitFor.visibility();

    ExpectedCondition<?> condition =
        visibility ? visibilityOfElementLocated(by) : invisibilityOfElementLocated(by);

    new FluentWait<>(driver)
        .withTimeout(Duration.of(timeout.time(), timeout.chronoUnit()))
        .withMessage(buildErrorMessage(clazz.getSimpleName(), by, visibility))
        .until(condition);
  }

  private static String buildErrorMessage(String pageName, By locator, boolean visibility) {
    StringBuilder builder =
        new StringBuilder()
            .append("Unable to open ")
            .append(pageName)
            .append(".\nLocator ")
            .append(locator)
            .append(" is ");

    return (visibility
            ? builder.append("not visible, but it should.")
            : builder.append("visible, but it shouldn't."))
        .toString();
  }
}
