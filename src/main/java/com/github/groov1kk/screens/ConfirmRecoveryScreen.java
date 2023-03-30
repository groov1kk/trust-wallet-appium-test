package com.github.groov1kk.screens;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

import com.github.groov1kk.core.BaseScreen;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ConfirmRecoveryScreen extends BaseScreen {

  private static final By WORDS_POSITION_LOCATOR = By.id("com.wallet.crypto.trustapp:id/position");
  private static final By WORDS_VALUE_LOCATOR = By.id("com.wallet.crypto.trustapp:id/value");

  @AndroidFindBy(
      xpath = "//*[@resource-id='com.wallet.crypto.trustapp:id/words']/android.widget.LinearLayout")
  private List<WebElement> words;

  @AndroidFindBy(
      xpath =
          "//*[@resource-id='com.wallet.crypto.trustapp:id/phrase']/android.widget.LinearLayout")
  private List<WebElement> typedWords;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/message")
  private WebElement typeMessage;

  @AndroidFindBy(id = "com.wallet.crypto.trustapp:id/action_done")
  private WebElement buttonDone;

  private final List<String> recoveryWords;

  protected ConfirmRecoveryScreen(AppiumDriver driver) {
    this(driver, Collections.emptyList());
  }

  protected ConfirmRecoveryScreen(AppiumDriver driver, List<String> recoveryWords) {
    super(driver);
    this.recoveryWords = Objects.requireNonNull(recoveryWords);
  }

  /**
   * Selects the word from the list and tap on it.
   *
   * @param word word to tap
   * @return {@code this} screen
   * @throws IllegalArgumentException if word is not present on the screen
   */
  public ConfirmRecoveryScreen tapWord(String word) {
    getWord(word).click();
    return this;
  }

  /**
   * Selects the words from the list and tap on them.
   *
   * @param words list of words to tap
   * @return {@code this} screen
   * @throws IllegalArgumentException if at least one word is not present on the screen
   */
  public ConfirmRecoveryScreen tapWords(String... words) {
    stream(words).forEach(this::tapWord);
    return this;
  }

  /**
   * Selects the words from the list and tap on them.
   *
   * @param words list of words to tap
   * @return {@code this} screen
   * @throws IllegalArgumentException if at least one word is not present on the screen
   */
  public ConfirmRecoveryScreen tapWords(Collection<String> words) {
    words.forEach(this::tapWord);
    return this;
  }

  /**
   * Returns words as index-word pair which were typed.
   *
   * @return index-word typed pairs
   */
  public Map<Integer, String> getTappedWords() {
    return typedWords.stream()
        .collect(
            toMap(
                word -> parseInt(word.findElement(WORDS_POSITION_LOCATOR).getText()),
                word -> word.findElement(WORDS_VALUE_LOCATOR).getText()));
  }

  /**
   * Returns a list of words that could be taped.
   *
   * @return list of words to tape
   */
  public List<String> getWords() {
    return words.stream()
        .map(word -> word.findElement(WORDS_VALUE_LOCATOR).getText())
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Returns a message that occurs during the typing words. It may be either error message, if the
   * order of typed words is wrong or the message that typing was successful. If message is absent
   * -{@code null} will be returned.
   *
   * @return tapping error message.
   */
  @Nullable
  public String getTypeMessage() {
    return typeMessage.isDisplayed() ? typeMessage.getText() : null;
  }

  /**
   * Checks, whether the "Done" button is enabled.
   *
   * @return is done button enabled
   */
  public boolean canConfirm() {
    return buttonDone.isEnabled();
  }

  /**
   * Taps the recovery words and presses "Done" button to confirm recovery.
   *
   * <p>To tap words this method uses recovery words from the clipboard, that were putted there by
   * pressing "Copy" button on the previous screen.
   */
  public void confirmRecovery() {
    confirmRecovery(null);
  }

  /**
   * Taps the recovery words and presses "Done" button to confirm recovery.
   *
   * <p>If the given words is {@code null} or empty, recovery words from the clipboard, that were
   * putted there by pressing "Copy" button on the previous screen, would be using.
   *
   * @param words words to tap
   * @throws IllegalArgumentException if both the given words and recovery words from the clipboard
   *     are empty or combination of these words is incorrect
   */
  public void confirmRecovery(@Nullable Collection<String> words) {
    words = Optional.ofNullable(words).orElse(recoveryWords);
    if (words.isEmpty()) {
      throw new IllegalStateException("To proceed you must pass recovery words");
    }

    tapWords(words);

    if (!canConfirm()) {
      throw new IllegalArgumentException(String.format("Combination of words %s is wrong", words));
    }

    buttonDone.click();
  }

  private WebElement getWord(String word) {
    return words.stream()
        .map(it -> it.findElement(WORDS_VALUE_LOCATOR))
        .filter(it -> it.getText().equals(word))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException(String.format("Unable to find word '%s'", word)));
  }
}
