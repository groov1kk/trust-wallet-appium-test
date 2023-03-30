package com.github.groov1kk.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public final class ReflectionUtils {

  private ReflectionUtils() {}

  public static <T> T newInstance(Class<T> clazz, Object... args) {
    Objects.requireNonNull(clazz, "Provided type must not be null");
    Class<?>[] argumentTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);

    Constructor<T> constructor = findConstructor(clazz, argumentTypes);
    if (constructor == null) {
      throw new RuntimeException(
          String.format(
              "Unable to find constructor: %s", getConstructorDescription(clazz, argumentTypes)));
    }

    try {
      constructor.setAccessible(true);
      return constructor.newInstance(args);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(String.format("Unable to instantiate %s", clazz.getName()), e);
    }
  }

  @Nullable
  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>[] argumentTypes) {
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      if (constructor.getParameterCount() != argumentTypes.length) {
        continue;
      }

      Class<?>[] parameterTypes = constructor.getParameterTypes();
      for (int i = 0; i < parameterTypes.length; i++) {
        if (!parameterTypes[i].isAssignableFrom(argumentTypes[i])) {
          continue;
        }
        return (Constructor<T>) constructor;
      }
    }
    return null;
  }

  private static String getConstructorDescription(Class<?> clazz, Class<?>[] argumentTypes) {
    String args =
        Arrays.stream(argumentTypes).map(Class::getName).collect(Collectors.joining(", "));
    return clazz.getName() + ".<init>(" + args + ")";
  }
}
