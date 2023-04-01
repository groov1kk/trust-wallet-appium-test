package com.github.groov1kk.core;

import com.github.groov1kk.utils.ReflectionUtils;
import com.google.common.collect.ObjectArrays;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.SlowLoadableComponent;

/** PageObject pattern representation. */
public abstract class BaseScreen extends SlowLoadableComponent<BaseScreen>
    implements WrapsDriver, SearchContext {

  protected final AppiumDriver driver;

  protected BaseScreen(AppiumDriver driver) {
    this(driver, Clock.systemDefaultZone(), Duration.ofSeconds(10));
  }

  protected BaseScreen(AppiumDriver driver, Clock clock, Duration timeout) {
    super(clock, timeout.toSecondsPart());
    this.driver = Objects.requireNonNull(driver, "Driver must not be null");
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
  }

  @Override
  public AppiumDriver getWrappedDriver() {
    return driver;
  }

  /**
   * Creates an instance of page object of specific type.
   *
   * @param clazz page object type
   * @param driver appium driver instance
   * @param args page object arguments
   * @return new page object instance
   * @param <T> page object type
   */
  protected static <T extends BaseScreen> T create(
      Class<T> clazz, AppiumDriver driver, Object... args) {
    return ReflectionUtils.newInstance(clazz, ObjectArrays.concat(driver, args));
  }

  /**
   * Alias for the {@link SlowLoadableComponent#get()} which does not require to cast the result
   * every time explicitly to a desired type.
   *
   * @return {@code this} page object
   * @param <T> type of the page object to return
   */
  @SuppressWarnings("unchecked")
  public <T extends BaseScreen> T waitForIt() {
    return (T) get();
  }

  @Override
  public WebElement findElement(By by) {
    return driver.findElement(by);
  }

  @Override
  public List<WebElement> findElements(By by) {
    return driver.findElements(by);
  }

  @Override
  protected void load() {
    // Do nothing by default
  }

  @Override
  protected void isLoaded() throws Error {
    // Do nothing by default
  }
}
