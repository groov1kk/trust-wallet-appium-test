package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.PageObjects;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class SecretPhraseBackupScreen extends BaseScreen {

  private static final String BACKUP_MANUALLY_LOCATOR = "//*[contains(@text, 'Back up manually')]";

  @AndroidFindBy(xpath = BACKUP_MANUALLY_LOCATOR)
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

  @Override
  protected void isLoaded() throws Error {
    PageObjects.requireElementVisible(this, AppiumBy.xpath(BACKUP_MANUALLY_LOCATOR));
  }
}
