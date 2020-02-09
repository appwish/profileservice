package io.appwish.profileservice.verticle;

import io.appwish.profileservice.repository.ProfileRepository;
import io.appwish.profileservice.service.DatabaseService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

/**
 * Verticle responsible for database access. Registers DatabaseService to expose the database on the
 * event bus.
 */
public class DatabaseVerticle extends AbstractVerticle {

  private final ProfileRepository profileRepository;

  public DatabaseVerticle(final ProfileRepository profileRepository) {
    this.profileRepository = profileRepository;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    final DatabaseService databaseService = new DatabaseService(vertx.eventBus(), profileRepository);
    databaseService.registerEventBusEventHandlers();
    startPromise.complete();
  }
}
