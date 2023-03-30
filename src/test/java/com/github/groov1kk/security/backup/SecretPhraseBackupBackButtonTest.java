package com.github.groov1kk.security.backup;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.SecretPhraseBackupScreen;
import com.github.groov1kk.screens.SetPasscodeScreen;
import com.github.groov1kk.screens.WelcomeScreen;
import com.github.groov1kk.security.SecurityBaseTest;
import org.testng.annotations.Test;

public class SecretPhraseBackupBackButtonTest extends SecurityBaseTest {

  private static final String WELCOME_SCREEN_TITLE = "Private and secure";
  private static final String PASSCODE_SCREEN_TITLE = "Create Passcode";

  private WelcomeScreen welcomeScreen;
  private SetPasscodeScreen setPasscodeScreen;
  private SecretPhraseBackupScreen secretPhraseBackupScreen;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);

    assertThat(welcomeScreen.getTitle())
        .as("Check welcome screen title")
        .isEqualTo(WELCOME_SCREEN_TITLE);
  }

  @Test(dependsOnMethods = "openApplication")
  public void createNewWallet() {
    setPasscodeScreen = welcomeScreen.createNewWallet();

    assertThat(setPasscodeScreen.getTitle())
            .as("Check passcode screen title")
            .isEqualTo(PASSCODE_SCREEN_TITLE);
  }

  @Test(dependsOnMethods = "createNewWallet")
  public void setPasscode() {
    secretPhraseBackupScreen = setPasscodeScreen.setPasscode(1, 2, 3, 4, 5, 6);
  }

  @Test(dependsOnMethods = "setPasscode")
  public void clickBackButton() {
    welcomeScreen = secretPhraseBackupScreen.back();

    assertThat(welcomeScreen.getTitle())
        .as("Check welcome screen title")
        .isEqualTo(WELCOME_SCREEN_TITLE);
  }

  @Test(dependsOnMethods = "clickBackButton")
  public void clickCreateNewWallet() {
    secretPhraseBackupScreen = welcomeScreen.clickCreateNewWallet(SecretPhraseBackupScreen.class);

    // TODO: need locators to verify title
  }
}
