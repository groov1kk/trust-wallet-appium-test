package com.github.groov1kk.security.rotate;

import static com.github.groov1kk.screens.WelcomeScreen.open;

import com.github.groov1kk.screens.WelcomeScreen;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.Test;

public class SwipeViewPagerLeftTest extends BaseRotateTest {

  private WelcomeScreen welcomeScreen;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);
  }

  @Test(dependsOnMethods = "openApplication")
  public void rotateLeft() {
    SoftAssertions softAssertions = new SoftAssertions();
    for (String title : EXPECTED_TITLES) {
      softAssertions
          .assertThat(welcomeScreen.getTitle())
          .as("Check image titles")
          .isEqualTo(title);

      welcomeScreen.swipeImageLeft();
    }
    softAssertions.assertAll();
  }
}
