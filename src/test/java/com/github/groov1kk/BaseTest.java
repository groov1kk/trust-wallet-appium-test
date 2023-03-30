package com.github.groov1kk;

import com.github.groov1kk.configuration.AppiumConfiguration;
import com.github.groov1kk.testng.AppiumServiceExecutionListener;
import io.appium.java_client.android.AndroidDriver;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

@Listeners(AppiumServiceExecutionListener.class)
@ContextConfiguration(classes = AppiumConfiguration.class)
public abstract class BaseTest extends AbstractTestNGSpringContextTests {

  @Autowired protected AndroidDriver driver;

  // Yes, I know that this is not good :) But sometimes we need it.
  protected static void pause(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
