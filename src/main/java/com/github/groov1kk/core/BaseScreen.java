package com.github.groov1kk.core;

import com.github.groov1kk.utils.ReflectionUtils;
import com.google.common.collect.ObjectArrays;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import java.util.Objects;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.support.PageFactory;

/** PageObject pattern representation. */
public abstract class BaseScreen implements WrapsDriver {

  protected final AppiumDriver driver;

  protected BaseScreen(AppiumDriver driver) {
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
   * Waits for a specific condition for page object. Can be used to prepare page object before
   * starting work with it.
   *
   * @return {@code this} page object
   * @param <T> returned page object type
   * @see com.github.groov1kk.core.annotations.AndroidWaitFor
   */
  @SuppressWarnings("unchecked")
  public <T extends BaseScreen> T waitForIt() {
    WaitingHelper.waitForScreen(this, driver);
    return (T) this;
  }
}
