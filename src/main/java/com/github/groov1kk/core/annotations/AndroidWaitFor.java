package com.github.groov1kk.core.annotations;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.WithTimeout;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AndroidWaitFor {

  /**
   * Locator for which this annotation has to wait for.
   *
   * @return android locator
   */
  AndroidFindBy value();

  /**
   * Determines, whether this annotation should wait for either visibility or invisibility of the
   * given locator.
   *
   * @return visibility or invisibility of this locator
   */
  boolean visibility() default true;

  /**
   * Determines the waiting time of the condition of this annotation.
   *
   * @return waiting time
   */
  WithTimeout timeout() default @WithTimeout(time = 5000, chronoUnit = ChronoUnit.SECONDS);
}
