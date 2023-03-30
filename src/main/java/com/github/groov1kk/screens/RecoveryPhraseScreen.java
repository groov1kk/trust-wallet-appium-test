package com.github.groov1kk.screens;

import static java.lang.Integer.parseInt;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toMap;

import com.github.groov1kk.core.BaseScreen;
import com.google.common.base.Splitter;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RecoveryPhraseScreen extends BaseScreen {

  private static final By WORD_POSITION_LOCATOR = By.id("com.wallet.crypto.trustapp:id/position");
  private static final By WORD_VALUE_LOCATOR = By.id("com.wallet.crypto.trustapp:id/value");

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/title")
  private WebElement title;

  @AndroidFindBy(
      xpath =
          "//*[@resource-id='com.wallet.crypto.trustapp:id/phrase']/android.widget.LinearLayout")
  private List<WebElement> words;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/action_copy")
  private WebElement buttonCopy;

  @AndroidFindBy(className = "android.widget.Toast")
  private WebElement toastCopy;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/action_verify")
  private WebElement buttonContinue;

  protected RecoveryPhraseScreen(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Returns the title of the screen.
   *
   * @return screen title
   */
  public String getTitle() {
    return title.getText();
  }

  /**
   * Returns all available words as a position-word pair.
   *
   * @return position-word pairs
   */
  public Map<Integer, String> getWords() {
    return words.stream()
        .collect(
            toMap(
                word -> parseInt(word.findElement(WORD_POSITION_LOCATOR).getText()),
                word -> word.findElement(WORD_VALUE_LOCATOR).getText()));
  }

  /**
   * Clicks "Copy" button to copy all available words into the clipboard.
   *
   * @return {@code this} screen
   */
  public RecoveryPhraseScreen copy() {
    buttonCopy.click();
    return this;
  }

  /**
   * Proceeds recovery phrase creation by clicking "Continue" button without copying all words.
   *
   * @return Confirm Recovery screen
   */
  public ConfirmRecoveryScreen proceed() {
    return proceed(false);
  }

  /**
   * Proceeds recovery phrase creation by clicking "Continue" button. If flag {@code shouldCopy} is
   * true, this method will copy all the given words into clipboard before proceeding.
   *
   * @param shouldCopy should click 'copy' button before proceeding
   * @return Confirm Recovery screen
   */
  public ConfirmRecoveryScreen proceed(boolean shouldCopy) {
    if (shouldCopy) {
      copy();
    }

    buttonContinue.click();

    List<String> recoveryWords = getRecoveryWordsFromClipboard(driver);
    return new ConfirmRecoveryScreen(driver, recoveryWords).waitForIt();
  }

  private static List<String> getRecoveryWordsFromClipboard(AppiumDriver driver) {
    if ((driver instanceof HasClipboard)) {
      String clipboardText = ((HasClipboard) driver).getClipboardText();
      return Splitter.on(" ").splitToList(clipboardText);
    }
    return emptyList();
  }
}
