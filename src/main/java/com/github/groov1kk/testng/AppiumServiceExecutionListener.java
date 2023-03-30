package com.github.groov1kk.testng;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.springframework.context.ApplicationContext;
import org.testng.IExecutionListener;
import org.testng.ITestResult;

public class AppiumServiceExecutionListener extends ContextAwareTestListener
    implements IExecutionListener {

  private AppiumDriverLocalService service;

  @Override
  public void onTestStart(ITestResult result) {
    // The service is singleton, and it's method start already uses Reentrant lock, so we do
    // not have to use additional synchronization here
    if (service == null) {
      ApplicationContext context = getApplicationContext(result);
      if (context != null) {
        service = context.getBean(AppiumDriverLocalService.class);
        service.start();
      }
    }
  }

  @Override
  public void onExecutionFinish() {
    if (service != null) {
      service.stop();
    }
  }
}
