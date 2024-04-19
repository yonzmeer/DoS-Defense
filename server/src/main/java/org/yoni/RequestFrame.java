package org.yoni;

import lombok.Getter;

public class RequestFrame {
  private final long creationTime = System.currentTimeMillis();
  @Getter private int requests = 1;

  public void increment() {
    requests++;
  }

  public boolean isDeprecated() {
    return creationTime + 5000 <= System.currentTimeMillis();
  }

  public boolean isValid() {
    return requests <= 5;
  }
}
