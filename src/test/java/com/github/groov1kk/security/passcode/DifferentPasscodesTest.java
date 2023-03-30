package com.github.groov1kk.security.passcode;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.SetPasscodeScreen;
import com.github.groov1kk.screens.WelcomeScreen;
import com.github.groov1kk.security.SecurityBaseTest;
import org.testng.annotations.Test;

public class DifferentPasscodesTest extends SecurityBaseTest {

  private static final String CREATE_PASSCODE_TITLE = "Create Passcode";
  private static final String CONFIRM_PASSCODE_TITLE = "Confirm Passcode";

  private static final String EXPECTED_ERROR_MESSAGE = "Those passwords didn’t match!";

  private WelcomeScreen welcomeScreen;
  private SetPasscodeScreen setPasscodeScreen;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);
  }

  @Test(dependsOnMethods = "openApplication")
  public void createNewWallet() {
    setPasscodeScreen = welcomeScreen.createNewWallet();

    assertThat(setPasscodeScreen.getTitle())
        .as("Check create passcode title")
        .isEqualTo(CREATE_PASSCODE_TITLE);
  }

  @Test(dependsOnMethods = "createNewWallet")
  public void createPasscode() {
    setPasscodeScreen.createPasscode(1, 2, 3, 4, 5, 6);

    assertThat(setPasscodeScreen.getTitle())
        .as("Check that title has changes")
        .isEqualTo(CONFIRM_PASSCODE_TITLE);
  }

  @Test(dependsOnMethods = "createPasscode")
  public void confirmPasscodeWithDifferentCode() {
    setPasscodeScreen.confirmPasscode(0, 9, 8, 7, 6, 5);

    pause(ofSeconds(1));

    assertThat(setPasscodeScreen.getPasscodeMessage())
        .as("Check error passcode message")
        .isEqualTo(EXPECTED_ERROR_MESSAGE);

    assertThat(setPasscodeScreen.getTitle())
        .as("Check that title hasn't changed")
        .isEqualTo(CREATE_PASSCODE_TITLE);
  }
}
