package com.github.groov1kk.configuration;

import static com.github.groov1kk.utils.PathUtils.toAbsolutePath;

import com.github.groov1kk.configuration.scope.ThreadLocal;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.SimpleThreadScope;

@Configuration
@PropertySource(name = "appium", value = "appium.properties")
@PropertySource(name = "environment specific", value = "${env:test}.properties")
public class AppiumConfiguration {

  @Bean
  public AppiumDriverLocalService appiumDriverLocalService(
      @Value("${appium.host}") String address, @Value("${appium.port}") int port) {
    return new AppiumServiceBuilder().withIPAddress(address).usingPort(port).build();
  }

  @Bean
  @ThreadLocal
  public AndroidDriver androidDriver(
      @Autowired AppiumDriverLocalService service,
      @Value("${device.name}") String deviceName,
      @Value("${app.path}") String appPath,
      @Value("${app.package}") String appPackage,
      @Value("${app.activity}") String appActivity) {
    UiAutomator2Options options =
        new UiAutomator2Options()
            .setApp(toAbsolutePath(appPath))
            .setDeviceName(deviceName)
            .autoGrantPermissions()
            .setAppWaitPackage(appPackage)
            .setAppWaitActivity(appActivity);

    return new AndroidDriver(service, options);
  }

  @Bean
  public static CustomScopeConfigurer customScopeConfigurer() {
    CustomScopeConfigurer configurer = new CustomScopeConfigurer();
    configurer.addScope(ThreadLocal.NAME, new SimpleThreadScope());
    return configurer;
  }
}
