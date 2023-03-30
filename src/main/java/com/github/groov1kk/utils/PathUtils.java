package com.github.groov1kk.utils;

import java.nio.file.Paths;
import java.util.Objects;

public final class PathUtils {

  private PathUtils() {}

  public static String toAbsolutePath(String path) {
    Objects.requireNonNull(path, "Path must not be null");
    return Paths.get(path).toAbsolutePath().toString();
  }
}
