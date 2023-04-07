package com.github.groov1kk.security.swipe;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.WelcomeScreen;
import org.testng.annotations.Test;

public class SwipeViewPagerRightTest extends BaseSwipeTest {

  private WelcomeScreen welcomeScreen;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);
  }

  @Test(dependsOnMethods = "openApplication")
  public void swipeViewerLeft() {
    String imageTitle = welcomeScreen.swipeImageLeft().getTitle();

    assertThat(imageTitle).as("Check image title").isEqualTo(EXPECTED_TITLES.get(1));
  }

  @Test(dependsOnMethods = "swipeViewerLeft")
  public void swipeViewerRight() {
    String imageTitle = welcomeScreen.swipeImageRight().getTitle();

    assertThat(imageTitle).as("Check image title").isEqualTo(EXPECTED_TITLES.get(0));
  }
}
