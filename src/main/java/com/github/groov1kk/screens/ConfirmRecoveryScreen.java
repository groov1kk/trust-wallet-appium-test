package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.widgets.Button;
import com.google.common.base.Preconditions;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
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
  private Button buttonDone;

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
   * @throws IllegalArgumentException if the {@code words} is empty or at least one of the words is
   *     not present on the screen
   */
  public ConfirmRecoveryScreen tapWords(String... words) {
    Preconditions.checkArgument(ArrayUtils.isNotEmpty(words), "Words must not be empty");
    Arrays.stream(words).forEach(this::tapWord);
    return this;
  }

  /**
   * Selects the words from the list and tap on them.
   *
   * @param words list of words to tap
   * @return {@code this} screen
   * @throws IllegalArgumentException if the {@code words} is empty or at least one of the words is
   *     not present on the screen
   */
  public ConfirmRecoveryScreen tapWords(Collection<String> words) {
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(words), "Words must not be empty");
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
            // Not recommended to use toUnmodifiableMap here due to the resulted map would have the
            // reversed order it's of keys
            Collectors.collectingAndThen(
                Collectors.toMap(
                    word -> Integer.parseInt(word.findElement(WORDS_POSITION_LOCATOR).getText()),
                    word -> word.findElement(WORDS_VALUE_LOCATOR).getText()),
                Collections::unmodifiableMap));
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
   * @throws IllegalStateException if the combination of either {@code words} or {@link
   *     #recoveryWords} is incorrect
   * @throws IllegalArgumentException if both the given words and the {@link #recoveryWords} are
   *     empty, or they contain at least one word that is not present on the screen
   */
  public void confirmRecovery(@Nullable Collection<String> words) {
    tapWords(Optional.ofNullable(words).orElse(recoveryWords));

    Preconditions.checkState(canConfirm(), "The combination of words is wrong: [%s]", words);
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
