package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.PageObjects;
import com.github.groov1kk.screens.widgets.PrivacyPolicies;
import com.github.groov1kk.screens.widgets.ViewPager;
import com.github.groov1kk.widgets.Button;
import com.google.common.base.Preconditions;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.time.Clock;
import java.time.Duration;

public class WelcomeScreen extends BaseScreen {

  private static final String CREATE_NEW_WALLET_BUTTON_LOCATOR =
      "com.wallet.crypto.trustapp:id/new_account_action";

  @AndroidFindBy(id = CREATE_NEW_WALLET_BUTTON_LOCATOR)
  private Button buttonCreateNewWallet;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/container")
  private PrivacyPolicies widgetPrivacyPolicies;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/pager")
  private ViewPager widgetViewPager;

  protected WelcomeScreen(AppiumDriver driver) {
    super(driver);
  }

  protected WelcomeScreen(AppiumDriver driver, Clock clock, Duration duration) {
    super(driver, clock, duration);
  }

  /**
   * Opens Trust Wallet application.
   *
   * @param driver instance of Appium Driver
   * @return Trust Wallet welcome screen
   */
  public static WelcomeScreen open(AppiumDriver driver) {
    return new WelcomeScreen(driver).waitForIt();
  }

  /**
   * Opens Trust Wallet application. Provides to specify waiting time when this screen would be
   * ready to work.
   *
   * @param driver instance of Appium Driver
   * @param clock clock instance to measure the timeout
   * @param timeout timeout in seconds
   * @return Trust Wallet welcome screen
   * @see org.openqa.selenium.support.ui.SlowLoadableComponent
   */
  public static WelcomeScreen open(AppiumDriver driver, Clock clock, Duration timeout) {
    return new WelcomeScreen(driver, clock, timeout).waitForIt();
  }

  /** Clicks on the "New Wallet" button. */
  public void clickCreateNewWallet() {
    buttonCreateNewWallet.click();
  }

  /**
   * Clicks on the "New Wallet" button.
   *
   * <p>Clicking this button may open different screens. This method allows you to specify which
   * screen has to be opened.
   *
   * @param screenType type of the returned screen
   * @return new screen
   * @param <T> Screen type
   */
  public <T extends BaseScreen> T clickCreateNewWallet(Class<T> screenType) {
    clickCreateNewWallet();
    return create(screenType, driver).waitForIt();
  }

  /**
   * Checks, whether the private policy widget is visible.
   *
   * @return is private policy fragment visible
   */
  public boolean arePrivatePoliciesVisible() {
    return widgetPrivacyPolicies.isDisplayed();
  }

  /**
   * Accepts private policies.
   *
   * @return {@code this} screen
   */
  public WelcomeScreen acceptPrivatePolicies() {
    Preconditions.checkState(arePrivatePoliciesVisible(), "Private policies are not visible");
    widgetPrivacyPolicies.accept();
    return this;
  }

  /**
   * Starts creating a new wallet by pressing "New Wallet" button.
   *
   * <p>This method implies that passcode has not been created yet. If passcode created, you have to
   * use {@link #clickCreateNewWallet( Class)} method with desired page object type instead.
   *
   * <p>If private policies fragment will be shown - this method will accept them.
   *
   * @return Set Passcode Screen
   */
  public SetPasscodeScreen createNewWallet() {
    clickCreateNewWallet();

    if (arePrivatePoliciesVisible()) {
      acceptPrivatePolicies();
    }

    return new SetPasscodeScreen(driver).waitForIt();
  }

  /**
   * Returns the current title of the screen.
   *
   * @return screen title
   */
  public String getTitle() {
    return widgetViewPager.getTitle();
  }

  /**
   * Swipes screen image left.
   *
   * @return {@code this} screen
   */
  public WelcomeScreen swipeImageLeft() {
    widgetViewPager.swipeLeft();
    return this;
  }

  /**
   * Swipe screen image right.
   *
   * @return {@code this} screen
   */
  public WelcomeScreen swipeImageRight() {
    widgetViewPager.swipeRight();
    return this;
  }

  /**
   * Checks, whether the image is present on the screen.
   *
   * @return is image present
   */
  public boolean hasImage() {
    return widgetViewPager.hasImage();
  }

  @Override
  protected void isLoaded() throws Error {
    PageObjects.requireVisibleElement(this, AppiumBy.id(CREATE_NEW_WALLET_BUTTON_LOCATOR));
  }
}
