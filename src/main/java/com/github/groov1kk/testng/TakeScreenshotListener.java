package com.github.groov1kk.testng;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.testng.IExecutionListener;
import org.testng.ITestResult;

public class TakeScreenshotListener extends ContextAwareTestListener implements IExecutionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(TakeScreenshotListener.class);

  private final File folder = new File("screenshots");

  @Override
  public void onExecutionStart() {
    File[] files = folder.listFiles();
    if (!folder.exists() || files == null) {
      return;
    }

    for (File file : files) {
      if (!file.delete()) {
        LOGGER.warn("Unable to delete screenshot {}", file.getName());
      }
    }
  }

  @Override
  public void onTestFailure(ITestResult result) {
    ApplicationContext context = getApplicationContext(result);
    if (context == null) {
      return;
    }

    if (!folder.exists()) {
      if (folder.mkdir()) {
        LOGGER.warn("Unable to create screenshots folder");
      }
    }

    try {
      TakesScreenshot takesScreenshot = context.getBean(TakesScreenshot.class);
      byte[] screenshotAsBytes = takesScreenshot.getScreenshotAs(OutputType.BYTES);
      File screenshot = new File(folder.getName(), generateFileName(result));
      Files.write(screenshotAsBytes, screenshot);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String generateFileName(ITestResult result) {
    return result.getTestClass().getXmlTest().getName().replaceAll(" ", "_")
        + "_"
        + UUID.randomUUID()
        + ".png";
  }
}
