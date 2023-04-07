package com.github.groov1kk.utils.actions;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

public class SwipeAction implements Action {

  private final WebDriver driver;
  private final int initialLength;
  private final Duration duration;
  private final PointerInput pointer;
  private final Point source;
  private final Point offset;

  private SwipeAction(Builder builder) {
    this.initialLength = builder.initialLength;
    this.driver = builder.driver;
    this.pointer = builder.pointer;
    this.duration = builder.duration;
    this.source = builder.source;
    this.offset = builder.offset;
  }

  @Override
  public void perform() {
    PointerInput.Origin origin = PointerInput.Origin.viewport();
    PointerInput.MouseButton button = PointerInput.MouseButton.LEFT;

    Sequence sequence =
        new Sequence(pointer, initialLength)
            .addAction(pointer.createPointerMove(Duration.ZERO, origin, source))
            .addAction(pointer.createPointerDown(button.asArg()))
            .addAction(pointer.createPointerMove(duration, origin, offset))
            .addAction(pointer.createPointerUp(button.asArg()));

    ((Interactive) driver).perform(Collections.singleton(sequence));
  }

  public static class Builder {

    private final WebDriver driver;

    private int initialLength;
    private Duration duration;
    private PointerInput pointer;
    private Point source;
    private Point offset;

    public Builder(WebDriver driver) {
      this.driver = Objects.requireNonNull(driver, "Driver must not be null");
      this.pointer = new PointerInput(PointerInput.Kind.TOUCH, "default_pointer");
      this.duration = Duration.ofMillis(200);
    }

    public Builder withPointer(PointerInput pointer) {
      this.pointer = Objects.requireNonNull(pointer, "Pointer input must not be null");
      return this;
    }

    public Builder withInitialLength(int length) {
      this.initialLength = length;
      return this;
    }

    public Builder withSource(Point source) {
      this.source = source;
      return this;
    }

    public Builder withOffset(Point offset) {
      this.offset = offset;
      return this;
    }

    public Builder withDuration(Duration duration) {
      this.duration = duration;
      return this;
    }

    public SwipeAction build() {
      return new SwipeAction(this);
    }
  }
}
