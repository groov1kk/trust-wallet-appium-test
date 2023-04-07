package com.github.groov1kk.testng;

import java.lang.reflect.Field;
import java.util.Arrays;
import javax.annotation.Nullable;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

abstract class ApplicationContextAwareTestListener extends TestListenerAdapter {

  /**
   * Extracts an application context from the given test result.
   *
   * @param result result of the test to search for context
   * @return spring application context
   */
  @Nullable
  protected static ApplicationContext getApplicationContext(ITestResult result) {
    return getApplicationContext(result.getInstance());
  }

  /**
   * Extracts an application context from a specific TestNG method.
   *
   * @param method TestNG's method to search for context.
   * @return spring application context
   */
  @Nullable
  protected static ApplicationContext getApplicationContext(ITestNGMethod method) {
    return getApplicationContext(method.getInstance());
  }

  @Nullable
  private static ApplicationContext getApplicationContext(Object instance) {
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

  /**
   * Extracts specific bean from the {@code context} by its type. This method avoids {@link
   * NoSuchBeanDefinitionException} in case of absence of a bean of desired type and returns {@code
   * null} instead.
   *
   * @param context application context
   * @param beanType desired bean type
   * @return bean from the context
   * @param <T> bean type
   */
  @Nullable
  protected static <T> T getBeanSafely(ApplicationContext context, Class<T> beanType) {
    try {
      return context.getBean(beanType);
    } catch (NoSuchBeanDefinitionException e) {
      return null;
    }
  }

  /**
   * Extracts an {@link ApplicationContext} from the given {@code result} and, if result contains
   * the context, extracts a specific bean from this context by its type. If the context is absent
   * or does not contain the bean of desired type - this method return {@code null}.
   *
   * <p>This method avoids {@link NoSuchBeanDefinitionException} in case of absence of a bean of
   * desired type and returns {@code null} instead.
   *
   * @param result result of the test to search for context
   * @param beanType desired bean type
   * @return bean from the context
   * @param <T> bean type
   */
  @Nullable
  protected static <T> T getBeanSafely(ITestResult result, Class<T> beanType) {
    ApplicationContext context = getApplicationContext(result);
    return context == null ? null : getBeanSafely(context, beanType);
  }

  /**
   * Extracts an {@link ApplicationContext} from the given {@code method} and, if the method
   * contains the context, extracts a specific bean from this context by its type. If the context is
   * absent or does not contain the bean of desired type - this method return {@code null}.
   *
   * <p>This method avoids {@link NoSuchBeanDefinitionException} in case of absence of a bean of
   * desired type and returns {@code null} instead.
   *
   * @param method TestNG's method to search for context
   * @param beanType desired bean type
   * @return bean from the context
   * @param <T> bean type
   */
  @Nullable
  protected static <T> T getBeanSafely(ITestNGMethod method, Class<T> beanType) {
    ApplicationContext context = getApplicationContext(method);
    return context == null ? null : getBeanSafely(context, beanType);
  }
}
