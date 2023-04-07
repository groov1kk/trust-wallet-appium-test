package com.github.groov1kk.utils.actions;

import static java.time.Duration.ofMillis;
import static java.util.Collections.singleton;
import static org.openqa.selenium.interactions.PointerInput.Origin.viewport;

import java.time.Duration;
import java.util.Objects;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public class SwipeAction implements Action {

  private final WebDriver driver;

  private int initialLength = 1;
  private Duration duration = ofMillis(200);
  private PointerInput pointer = defaultPointer();
  private Point source;
  private Point offset;

  public SwipeAction(WebDriver driver) {
    this.driver = Objects.requireNonNull(driver);
  }

  public SwipeAction withPointer(PointerInput pointer) {
    this.pointer = Objects.requireNonNull(pointer);
    return this;
  }

  public SwipeAction withInitialLength(int length) {
    this.initialLength = length;
    return this;
  }

  public SwipeAction withSource(Point source) {
    this.source = source;
    return this;
  }

  public SwipeAction withOffset(Point offset) {
    this.offset = offset;
    return this;
  }

  public SwipeAction withDuration(Duration duration) {
    this.duration = duration;
    return this;
  }

  @Override
  public void perform() {
    Sequence sequence =
        new Sequence(pointer, initialLength)
            .addAction(pointer.createPointerMove(ofMillis(0), viewport(), source))
            .addAction(pointer.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
            .addAction(pointer.createPointerMove(duration, viewport(), offset))
            .addAction(pointer.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

    ((Interactive) driver).perform(singleton(sequence));
  }

  private static PointerInput defaultPointer() {
    return new PointerInput(PointerInput.Kind.TOUCH, "default_pointer");
  }
}
