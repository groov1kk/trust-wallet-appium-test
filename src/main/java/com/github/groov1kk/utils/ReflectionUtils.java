package com.github.groov1kk.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public final class ReflectionUtils {

  private ReflectionUtils() {}

  /**
   * Creates a new instance of desired type with the given arguments.
   *
   * <p>There are several differences between this method and the {@link
   * Constructor#newInstance(Object...)}:
   *
   * <ul>
   *   <li>This method can create new instances using protected and private constructors by default.
   *   <li>This method also tries to invoke constructors with inherited types of arguments.
   * </ul>
   *
   * @param clazz desired type of new instance
   * @param args instance arguments
   * @return new instance
   * @param <T> instance type
   */
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

  /**
   * Looks for a constructor of specific type with the given argument types. This method is not
   * looking for constructors with exactly the same set of argument types but also is looking for
   * constructors which argument types are child types of the given argument types.
   *
   * @param clazz desired constructor type
   * @param argumentTypes types of the constructor's arguments
   * @return constructor or {@code null}
   * @param <T> constructor type
   */
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
