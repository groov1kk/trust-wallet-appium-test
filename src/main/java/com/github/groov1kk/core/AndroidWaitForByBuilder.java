package com.github.groov1kk.core;

import com.github.groov1kk.core.annotations.AndroidWaitFor;
import com.google.common.collect.Lists;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

class AndroidWaitForByBuilder extends AbstractAnnotations {

  private final AndroidFindBy androidFindBy;

  AndroidWaitForByBuilder(AndroidWaitFor androidWaitFor) {
    this.androidFindBy = Objects.requireNonNull(androidWaitFor).value();
  }

  @Override
  public By buildBy() {
    assertValidFindBy();
    return findLocator();
  }

  @Override
  public boolean isLookupCached() {
    return false;
  }

  private void assertValidFindBy() {
    List<String> strategies = Lists.newArrayList();

    String uiAutomator = androidFindBy.uiAutomator();
    if (!uiAutomator.isEmpty()) {
      strategies.add("UIAutomator: " + uiAutomator);
    }

    String accessibility = androidFindBy.accessibility();
    if (!accessibility.isEmpty()) {
      strategies.add("Accessibility: " + accessibility);
    }

    String id = androidFindBy.id();
    if (!id.isEmpty()) {
      strategies.add("Id: " + id);
    }

    String className = androidFindBy.className();
    if (!className.isEmpty()) {
      strategies.add("Class name: " + className);
    }

    String tagName = androidFindBy.tagName();
    if (!tagName.isEmpty()) {
      strategies.add("Tag name: " + tagName);
    }

    String dataMatcher = androidFindBy.androidDataMatcher();
    if (!dataMatcher.isEmpty()) {
      strategies.add("Data matcher: " + dataMatcher);
    }

    String viewMatcher = androidFindBy.androidViewMatcher();
    if (!viewMatcher.isEmpty()) {
      strategies.add("View matcher: " + viewMatcher);
    }

    String xpath = androidFindBy.xpath();
    if (!xpath.isEmpty()) {
      strategies.add("Xpath: " + xpath);
    }

    if (strategies.size() > 1) {
      throw new IllegalArgumentException(
          String.format(
              "You must specify at most one location strategy. Number found: %d (%s)",
              strategies.size(), strategies));
    }
  }

  private By findLocator() {
    String uiAutomator = androidFindBy.uiAutomator();
    if (!uiAutomator.isEmpty()) {
      return AppiumBy.androidUIAutomator(uiAutomator);
    }

    String accessibility = androidFindBy.accessibility();
    if (!accessibility.isEmpty()) {
      return AppiumBy.accessibilityId(accessibility);
    }

    String id = androidFindBy.id();
    if (!id.isEmpty()) {
      return AppiumBy.id(id);
    }

    String className = androidFindBy.className();
    if (!className.isEmpty()) {
      return AppiumBy.className(className);
    }

    String tagName = androidFindBy.tagName();
    if (!tagName.isEmpty()) {
      return AppiumBy.tagName(tagName);
    }

    String dataMatcher = androidFindBy.androidDataMatcher();
    if (!dataMatcher.isEmpty()) {
      return AppiumBy.androidDataMatcher(dataMatcher);
    }

    String viewMatcher = androidFindBy.androidViewMatcher();
    if (!viewMatcher.isEmpty()) {
      return AppiumBy.androidViewMatcher(viewMatcher);
    }

    return AppiumBy.xpath(androidFindBy.xpath());
  }
}
