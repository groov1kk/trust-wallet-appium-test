package com.github.groov1kk.testng;

import static com.github.groov1kk.utils.PathUtils.toAbsolutePath;

import io.appium.java_client.InteractsWithApps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.testng.ITestResult;

/** Re-installs and restarts an application before each XML test. */
public class ReinstallApplicationListener extends ApplicationContextAwareTestListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReinstallApplicationListener.class);

  private volatile boolean shouldReinstall = true;

  @Override
  public void onTestStart(ITestResult result) {
    ApplicationContext context = getApplicationContext(result);
    if (context == null) {
      return;
    }

    if (shouldReinstall) {
      synchronized (this) {
        if (shouldReinstall) {
          InteractsWithApps appInteractor = getBeanSafely(context, InteractsWithApps.class);
          if (appInteractor == null) {
            LOGGER.warn("Unable to reinstall the application");
            return;
          }

          Environment environment = context.getEnvironment();
          String bundleId = environment.getProperty("app.package");
          String appPath = toAbsolutePath(environment.getProperty("app.path"));

          appInteractor.removeApp(bundleId);
          appInteractor.installApp(appPath);
          appInteractor.activateApp(bundleId);

          shouldReinstall = false;
        }
      }
    }
  }
}
