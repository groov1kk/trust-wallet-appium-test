package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.annotations.AndroidWaitFor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

@AndroidWaitFor(@AndroidFindBy(xpath = "//*[contains(@text, 'Back up manually')]"))
public class SecretPhraseBackupScreen extends BaseScreen {

  @AndroidFindBy(xpath = "//*[contains(@text, 'Back up manually')]")
  private WebElement backupManually;

  @AndroidFindBy(xpath = "//android.view.View[@content-desc='Back']")
  private WebElement buttonBack;

  protected SecretPhraseBackupScreen(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Clicks "Backup manually".
   *
   * @return Consent screen
   */
  public ConsentScreen backupManually() {
    backupManually.click();
    return new ConsentScreen(driver).waitForIt();
  }

  /**
   * Returns to the Welcome screen by clicking the back button.
   *
   * @return Welcome screen
   */
  public WelcomeScreen back() {
    buttonBack.click();
    return new WelcomeScreen(driver).waitForIt();
  }
}
