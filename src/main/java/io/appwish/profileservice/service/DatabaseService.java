package io.appwish.profileservice.service;

import io.appwish.profileservice.eventbus.Address;
import io.appwish.profileservice.repository.ProfileRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * Exposes the user profile repository on the event bus. Takes data from the user profile repository and replies to
 * requests on the event bus.
 */
public class DatabaseService {

  private final EventBus eventBus;
  private final ProfileRepository profileRepository;

  public DatabaseService(final EventBus eventBus, final ProfileRepository profileRepository) {
    this.eventBus = eventBus;
    this.profileRepository = profileRepository;
  }

  public void registerEventBusEventHandlers() {
    eventBus.<Void>consumer(Address.TEST_ADDRESS.get())
      .handler(event -> profileRepository.test().setHandler(
        testHandler(event))); // TODO remove test code when implementing storing profiles
  }

  private Handler<AsyncResult<String>> testHandler(final Message<Void> event) { // TODO remove test code when implementing storing profiles
    return query -> {
      if (query.succeeded()) {
        event.reply(query.result());
      } else {
        event.fail(1, "Error fetching profiles from the database");
      }
    };
  }
}
