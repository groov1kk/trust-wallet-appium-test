package com.github.groov1kk.security.rotate;

import static com.github.groov1kk.screens.WelcomeScreen.open;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.groov1kk.screens.WelcomeScreen;
import org.testng.annotations.Test;

public class SwipeViewPagerRightTest extends BaseRotateTest {

  private WelcomeScreen welcomeScreen;

  @Test
  public void openApplication() {
    welcomeScreen = open(driver);
  }

  @Test(dependsOnMethods = "openApplication")
  public void rotateViewerLeft() {
    String imageTitle = welcomeScreen.swipeImageLeft().getTitle();

    assertThat(imageTitle).as("Check image title").isEqualTo(EXPECTED_TITLES.get(1));
  }

  @Test(dependsOnMethods = "rotateViewerLeft")
  public void rotateViewerRight() {
    String imageTitle = welcomeScreen.swipeImageRight().getTitle();

    assertThat(imageTitle).as("Check image title").isEqualTo(EXPECTED_TITLES.get(0));
  }
}
