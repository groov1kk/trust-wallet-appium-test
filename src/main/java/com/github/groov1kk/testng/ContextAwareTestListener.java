package com.github.groov1kk.testng;

import java.lang.reflect.Field;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

abstract class ContextAwareTestListener extends TestListenerAdapter {

  /**
   * Extracts an application context from the given test result.
   *
   * @param result result of the test to search for context
   * @return spring application context
   */
  @Nullable
  public final ApplicationContext getApplicationContext(ITestResult result) {
    return getApplicationContext(result.getInstance());
  }

  /**
   * Extracts an application context from a specific TestNG method.
   *
   * @param method TestNG's method to search for context.
   * @return spring application context
   */
  @Nullable
  public final ApplicationContext getApplicationContext(ITestNGMethod method) {
    return getApplicationContext(method.getInstance());
  }

  @Nullable
  private ApplicationContext getApplicationContext(Object instance) {
    Class<?> clazz = instance.getClass();
    if (!AbstractTestNGSpringContextTests.class.isAssignableFrom(clazz)) {
      return null;
    }

    while (clazz != AbstractTestNGSpringContextTests.class) {
      clazz = clazz.getSuperclass();
    }

    return Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> ApplicationContext.class.isAssignableFrom(field.getType()))
        .findFirst()
        .map(field -> getContextFromField(field, instance))
        .orElse(null);
  }

  private static ApplicationContext getContextFromField(Field field, Object instance) {
    field.setAccessible(true);
    try {
      return (ApplicationContext) field.get(instance);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
