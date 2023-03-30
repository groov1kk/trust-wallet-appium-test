package com.github.groov1kk.security;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.ConfirmRecoveryScreen;
import com.github.groov1kk.screens.ConsentScreen;
import com.github.groov1kk.screens.RecoveryPhraseScreen;
import com.github.groov1kk.screens.SecretPhraseBackupScreen;
import com.github.groov1kk.screens.SetPasscodeScreen;
import com.github.groov1kk.screens.WelcomeScreen;
import com.google.common.base.Splitter;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class SecurityHappyPathTest extends SecurityBaseTest {

  private static final int[] CORRECT_PASSPHRASE = {1, 2, 3, 4, 5, 6};

  private static final String WELCOME_SCREEN_TITLE = "Private and secure";
  private static final String PASSCODE_SCREEN_TITLE = "Create Passcode";
  private static final String RECOVERY_SCREEN_TITLE = "Your Secret Phrase";

  private WelcomeScreen welcomeScreen;
  private SetPasscodeScreen setPasscodeScreen;
  private SecretPhraseBackupScreen secretPhraseBackupScreen;
  private ConsentScreen consentScreen;
  private RecoveryPhraseScreen recoveryPhraseScreen;
  private ConfirmRecoveryScreen confirmRecoveryScreen;

  private Map<Integer, String> words;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);

    assertThat(welcomeScreen.getTitle())
        .as("Check the title of welcome screen")
        .isEqualTo(WELCOME_SCREEN_TITLE);

    assertThat(welcomeScreen.hasImage()).as("Check that image is present on the screen").isTrue();
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
    secretPhraseBackupScreen = setPasscodeScreen.setPasscode(CORRECT_PASSPHRASE);
  }

  @Test(dependsOnMethods = "setPasscode")
  public void selectBackupManually() {
    consentScreen = secretPhraseBackupScreen.backupManually();

    assertThat(consentScreen.canProceed())
        .as("Check that button Continue is disabled by default")
        .isFalse();
  }

  @Test(dependsOnMethods = "selectBackupManually")
  public void applyAllPolicies() {
    consentScreen.applyAllPolicies();

    assertThat(consentScreen.canProceed())
        .as("Check that applying all policies button Continue is clickable")
        .isTrue();
  }

  @Test(dependsOnMethods = "applyAllPolicies")
  public void pressContinue() {
    recoveryPhraseScreen = consentScreen.proceed();

    assertThat(recoveryPhraseScreen.getTitle())
        .as("Check recovery phrase screen title")
        .isEqualTo(RECOVERY_SCREEN_TITLE);
  }

  @Test(dependsOnMethods = "pressContinue")
  public void copyRecoveryWords() {
    words = recoveryPhraseScreen.copy().getWords();

    assertThat(getWordsFromClipboard())
        .as("Check that the words were correctly copied to the clipboard")
        .containsExactlyElementsOf(words.values());
  }

  @Test(dependsOnMethods = "copyRecoveryWords")
  public void proceedCreateRecovery() {
    confirmRecoveryScreen = recoveryPhraseScreen.proceed();

    assertThat(confirmRecoveryScreen.getWords())
        .as("Check that confirm recovery screen contains the same words")
        .containsExactlyInAnyOrderElementsOf(words.values());
  }

  @Test(dependsOnMethods = "proceedCreateRecovery")
  public void typeRecoveryWords() {
    confirmRecoveryScreen.tapWords(getWordsFromClipboard());

    assertThat(confirmRecoveryScreen.getTappedWords())
        .as("Check that tapped words shown")
        .containsAllEntriesOf(words);

    assertThat(confirmRecoveryScreen.getTypeMessage())
        .as("Check type message")
        .isEqualTo("Well done! "); // Message really contains space, this is a bug

    assertThat(confirmRecoveryScreen.canConfirm())
        .as("Check that confirm button is enabled")
        .isTrue();
  }

  private List<String> getWordsFromClipboard() {
    return Splitter.on(" ").splitToList(driver.getClipboardText());
  }
}
