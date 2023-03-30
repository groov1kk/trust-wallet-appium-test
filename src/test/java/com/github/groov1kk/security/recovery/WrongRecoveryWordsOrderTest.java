package com.github.groov1kk.security.recovery;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.ConfirmRecoveryScreen;
import com.github.groov1kk.screens.ConsentScreen;
import com.github.groov1kk.screens.RecoveryPhraseScreen;
import com.github.groov1kk.screens.SecretPhraseBackupScreen;
import com.github.groov1kk.screens.SetPasscodeScreen;
import com.github.groov1kk.screens.WelcomeScreen;
import com.github.groov1kk.security.SecurityBaseTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

public class WrongRecoveryWordsOrderTest extends SecurityBaseTest {

  private static final String WELCOME_SCREEN_TITLE = "Private and secure";
  private static final String EXPECTED_RECOVERY_MESSAGE = "Invalid order. Try again!";

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
        .as("Check welcome screen title")
        .isEqualTo(WELCOME_SCREEN_TITLE);
  }

  @Test(dependsOnMethods = "openApplication")
  public void createNewWallet() {
    setPasscodeScreen = welcomeScreen.createNewWallet();
  }

  @Test(dependsOnMethods = "createNewWallet")
  public void setPasscode() {
    secretPhraseBackupScreen = setPasscodeScreen.setPasscode(1, 2, 3, 4, 5, 6);
  }

  @Test(dependsOnMethods = "setPasscode")
  public void selectBackupManually() {
    consentScreen = secretPhraseBackupScreen.backupManually();

    assertThat(consentScreen.canProceed())
        .as("Check that button Continue is disabled by default")
        .isFalse();
  }

  @Test(dependsOnMethods = "selectBackupManually")
  public void applyAllPoliciesAndProceed() {
    recoveryPhraseScreen = consentScreen.applyAllPolicies().proceed();
  }

  @Test(dependsOnMethods = "applyAllPoliciesAndProceed")
  public void proceedRecoveryWithoutCopyingWords() {
    words = recoveryPhraseScreen.getWords();
    confirmRecoveryScreen = recoveryPhraseScreen.proceed();
  }

  @Test(dependsOnMethods = "proceedRecoveryWithoutCopyingWords")
  public void typeRecoveryWordsInWrongOrder() {
    confirmRecoveryScreen.tapWords(shuffleWords(words.values()));

    assertThat(confirmRecoveryScreen.canConfirm())
        .as("Check that 'Done' button is not editable")
        .isFalse();

    assertThat(confirmRecoveryScreen.getTypeMessage())
        .as("Check type message")
        .isEqualTo(EXPECTED_RECOVERY_MESSAGE);
  }

  private static List<String> shuffleWords(Collection<String> words) {
    List<String> result = new ArrayList<>(words);
    Collections.shuffle(result);
    return result;
  }
}
