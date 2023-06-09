package com.github.groov1kk.screens;

import com.github.groov1kk.core.BaseScreen;
import com.github.groov1kk.core.PageObjects;
import com.github.groov1kk.widgets.CustomWidget;
import com.github.groov1kk.widgets.Text;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.Arrays;
import java.util.List;

public class SetPasscodeScreen extends BaseScreen {

  public static final String CREATE_PASSCODE_TITLE = "Create Passcode";
  public static final String CONFIRM_PASSCODE_TITLE = "Confirm Passcode";

  private static final Range<Integer> NUMBER_RANGE = Range.closed(0, 9);

  // Too fragile locators, I know :(. If I can, I would ask developers to add accessibility locators
  // here.
  private static final String TITLE_LOCATOR =
      "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.TextView[1]";

  @AndroidFindBy(xpath = "//android.widget.TextView[matches(@text, '\\d')]")
  private List<Text> digits;

  @AndroidFindBy(xpath = TITLE_LOCATOR)
  private Text title;

  @AndroidFindBy(
      xpath =
          "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.TextView[2]")
  // does not work when message is absent. Need additional locator here
  private Text passcodeMessage;

  protected SetPasscodeScreen(AppiumDriver driver) {
    super(driver);
  }

  /**
   * Presses a specific number on the numeric keyboard.
   *
   * @param number number to press
   * @return {@code this} screen
   * @throws IllegalArgumentException if the given number is not enclosed in [0-9] range.
   */
  public SetPasscodeScreen pressNumber(int number) {
    Preconditions.checkArgument(NUMBER_RANGE.contains(number), "Number must be in [0-9] range");
    digits.stream()
        .filter(digit -> Integer.parseInt(digit.getText()) == number)
        .forEach(CustomWidget::click);
    return this;
  }

  /**
   * Presses numbers on the numeric keyboard.
   *
   * @param numbers numbers to press
   * @return {@code this} screen
   * @throws IllegalArgumentException if at least one number of the given numbers is not enclosed in
   *     [0-9] range.
   */
  public SetPasscodeScreen pressNumbers(int... numbers) {
    Arrays.stream(numbers).forEach(this::pressNumber);
    return this;
  }

  /**
   * Types the given passcode twice to create and confirm passcode. If everything passes correct,
   * Secret Phrase Backup screen will be opened.
   *
   * @param passcode passcode to type twice
   * @return Secret Phrase Backup screen
   * @throws IllegalArgumentException if at least one number of the given passcode is not enclosed
   *     in [0-9] range, or the length of passcode does not match 6.
   */
  public SecretPhraseBackupScreen setPasscode(int... passcode) {
    createPasscode(passcode).confirmPasscode(passcode);
    return new SecretPhraseBackupScreen(driver).waitForIt();
  }

  /**
   * Types the passcode for the first time to create it. If passcode has already been typed and
   * application asks you to confirm passcode - the method will throw {@link IllegalStateException}.
   *
   * @param passcode passcode to create
   * @return {@code this} Set Passcode screen
   * @throws IllegalStateException if a passcode has already been typed
   * @throws IllegalArgumentException if at least one number of the given passcode is not enclosed
   *     in [0-9] range, or the length of passcode does not match 6.
   */
  public SetPasscodeScreen createPasscode(int... passcode) {
    Preconditions.checkState(
        CREATE_PASSCODE_TITLE.equals(getTitle()), "Passcode has already been typed");
    return pressNumbers(checkPasscode(passcode)).waitForPasscodeCreation();
  }

  /**
   * Types the passcode for the second time to confirm it. If passcode has not been typed yet and
   * application asks you to create passcode - the method will throw {@link IllegalStateException}.
   *
   * @param passcode passcode to confirm
   * @return {@code this} Set Passcode screen
   * @throws IllegalStateException if a passcode has not been typed yet
   * @throws IllegalArgumentException if at least one number of the given passcode is not enclosed
   *     in [0-9] range, or the length of passcode does not match 6.
   */
  public SetPasscodeScreen confirmPasscode(int... passcode) {
    Preconditions.checkState(
        CONFIRM_PASSCODE_TITLE.equals(getTitle()), "Passcode has not been typed yet");
    return pressNumbers(checkPasscode(passcode));
  }

  /**
   * Returns the current screen's title.
   *
   * @return screen title
   */
  public String getTitle() {
    return title.getText();
  }

  /**
   * Return the result passcode typing message.
   *
   * @return passcode result message
   */
  public String getPasscodeMessage() {
    // Usually appears with a delay
    return waitFor(passcodeMessage).until(CustomWidget::getText);
  }

  private SetPasscodeScreen waitForPasscodeCreation() {
    waitFor(this).until(it -> it.getTitle().equals(CONFIRM_PASSCODE_TITLE));
    return this;
  }

  private static int[] checkPasscode(int[] passcode) {
    Preconditions.checkState(passcode.length == 6, "Passcode must exactly contains 6 digits");
    return passcode;
  }

  @Override
  protected void isLoaded() throws Error {
    PageObjects.requireVisibleElement(this, AppiumBy.xpath(TITLE_LOCATOR));
  }
}
