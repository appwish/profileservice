package io.appwish.profileservice.eventbus;

/**
 * Represents addresses available on the event bus
 */
public enum Address {
  TEST_ADDRESS; // TODO remove test code when implementing storing profiles

  public String get() {
    return name();
  }
}
