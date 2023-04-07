package com.github.groov1kk.screens;

import static io.appium.java_client.AppiumBy.id;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.PageObjects;
import com.github.groov1kk.widgets.Button;
import com.github.groov1kk.widgets.Checkbox;
import com.google.common.base.Preconditions;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class ConsentScreen extends BaseScreen {

  private static final String BUTTON_CONTINUE_LOCATOR = "com.wallet.crypto.trustapp:id/next";

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent1']//android.widget.CheckBox")
  private Checkbox checkboxILoseMySecretPhrase;

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent2']//android.widget.CheckBox")
  private Checkbox checkboxIExposeMySecretPhrase;

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent3']//android.widget.CheckBox")
  private Checkbox checkboxITrustWalletSupport;

  @AndroidFindBy(id = BUTTON_CONTINUE_LOCATOR)
  private Button buttonContinue;

  protected ConsentScreen(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Checks "I lose my secret phrase" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkILoseMySecretPhrase() {
    checkboxILoseMySecretPhrase.check();
    return this;
  }

  /**
   * Checks "I expose my secret phrase" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkIExposeMySecretPhrase() {
    checkboxIExposeMySecretPhrase.check();
    return this;
  }

  /**
   * Checks "I trust wallet support" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkITrustWalletSupport() {
    checkboxITrustWalletSupport.check();
    return this;
  }

  /**
   * Checks all three checkboxes.
   *
   * @return {@code this} screen
   */
  public ConsentScreen applyAllPolicies() {
    checkILoseMySecretPhrase().checkIExposeMySecretPhrase().checkITrustWalletSupport();
    return this;
  }

  /**
   * Proceeds by clicking button "Continue".
   *
   * @return Recovery Phrase screen
   */
  public RecoveryPhraseScreen proceed() {
    Preconditions.checkState(canProceed(), "Button 'Continue' is not clickable");
    buttonContinue.click();
    return new RecoveryPhraseScreen(driver).waitForIt();
  }

  /**
   * Checks, whether the button "Continue" is clickable.
   *
   * @return is continue button clickable
   */
  public boolean canProceed() {
    return buttonContinue.isEnabled();
  }

  @Override
  protected void isLoaded() throws Error {
    PageObjects.requireVisibleElement(this, AppiumBy.id(BUTTON_CONTINUE_LOCATOR));
  }
}
