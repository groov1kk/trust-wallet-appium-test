package com.github.groov1kk.testng;

import static com.github.groov1kk.utils.PathUtils.toAbsolutePath;

import io.appium.java_client.InteractsWithApps;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.testng.ITestContext;

/** Re-installs and restarts an application after each test. */
public class ReinstallApplicationListener extends ContextAwareTestListener {

  @Override
  public void onFinish(ITestContext testContext) {
    ApplicationContext context = getApplicationContext(testContext);
    if (context == null) {
      return;
    }

    InteractsWithApps appInteractor = getApplicationInteractor(context);
    if (appInteractor != null) {
      Environment environment = context.getEnvironment();
      String bundleId = environment.getProperty("app.package");
      String appPath = toAbsolutePath(environment.getProperty("app.path"));

      appInteractor.removeApp(bundleId);
      appInteractor.installApp(appPath);
      appInteractor.activateApp(bundleId);
    }
  }

  @Nullable
  private ApplicationContext getApplicationContext(ITestContext testContext) {
    return Arrays.stream(testContext.getAllTestMethods())
        .map(this::getApplicationContext)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  @Nullable
  private InteractsWithApps getApplicationInteractor(ApplicationContext context) {
    try {
      return context.getBean(InteractsWithApps.class);
    } catch (NoSuchBeanDefinitionException e) {
      return null;
    }
  }
}
