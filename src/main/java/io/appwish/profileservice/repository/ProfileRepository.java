package io.appwish.profileservice.repository;

import io.vertx.core.Future;

/**
 * Interface for interaction with profile persistence layer
 */
public interface ProfileRepository {

  Future<String> test(); // TODO remove test code when implementing storing profiles
}
