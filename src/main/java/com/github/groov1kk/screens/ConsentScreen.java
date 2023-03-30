package com.github.groov1kk.screens;

import static com.github.groov1kk.utils.WebElementUtils.check;
import static com.google.common.base.Preconditions.checkState;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.annotations.AndroidWaitFor;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

@AndroidWaitFor(@AndroidFindBy(id = "com.wallet.crypto.trustapp:id/next"))
public class ConsentScreen extends BaseScreen {

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent1']//android.widget.CheckBox")
  private WebElement checkboxILoseMySecretPhrase;

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent2']//android.widget.CheckBox")
  private WebElement checkboxIExposeMySecretPhrase;

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/concent3']//android.widget.CheckBox")
  private WebElement checkboxITrustWalletSupport;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/next")
  private WebElement buttonContinue;

  protected ConsentScreen(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Checks "I lose my secret phrase" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkILoseMySecretPhrase() {
    check(checkboxILoseMySecretPhrase);
    return this;
  }

  /**
   * Checks "I expose my secret phrase" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkIExposeMySecretPhrase() {
    check(checkboxIExposeMySecretPhrase);
    return this;
  }

  /**
   * Checks "I trust wallet support" checkbox.
   *
   * @return {@code this} screen
   */
  public ConsentScreen checkITrustWalletSupport() {
    check(checkboxITrustWalletSupport);
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
    checkState(canProceed(), "Button 'Continue' is not clickable");
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
}
