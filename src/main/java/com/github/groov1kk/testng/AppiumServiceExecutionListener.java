package com.github.groov1kk.testng;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import java.util.Optional;
import org.testng.IExecutionListener;
import org.testng.ITestResult;

public class AppiumServiceExecutionListener extends ApplicationContextAwareTestListener
    implements IExecutionListener {

  private AppiumDriverLocalService service;

  @Override
  public void onTestStart(ITestResult result) {
    // The service is singleton, and it's method start already uses Reentrant lock, so we do
    // not have to use additional synchronization here
    Optional.ofNullable(getBeanSafely(result, AppiumDriverLocalService.class))
        .ifPresent(
            service -> {
              service.start();
              this.service = service;
            });
  }

  @Override
  public void onExecutionFinish() {
    if (service != null) {
      service.stop();
    }
  }
}
