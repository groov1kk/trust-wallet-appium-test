package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.widgets.Button;
import com.github.groov1kk.widgets.Text;
import com.google.common.base.Splitter;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RecoveryPhraseScreen extends BaseScreen {

  private static final By WORD_POSITION_LOCATOR = By.id("com.wallet.crypto.trustapp:id/position");
  private static final By WORD_VALUE_LOCATOR = By.id("com.wallet.crypto.trustapp:id/value");

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/title")
  private Text title;

  @AndroidFindBy(
      xpath =
          "//*[@resource-id='com.wallet.crypto.trustapp:id/phrase']/android.widget.LinearLayout")
  private List<Text> words;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/action_copy")
  private Text buttonCopy;

  @AndroidFindBy(className = "android.widget.Toast")
  private WebElement toastCopy;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/action_verify")
  private Button buttonContinue;

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
            // Not recommended to use toUnmodifiableMap here due to the resulted map would have the
            // reversed order it's of keys
            Collectors.collectingAndThen(
                Collectors.toMap(
                    word -> Integer.parseInt(word.findElement(WORD_POSITION_LOCATOR).getText()),
                    word -> word.findElement(WORD_VALUE_LOCATOR).getText()),
                Collections::unmodifiableMap));
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
    return Collections.emptyList();
  }
}
