package io.appwish.profileservice.repository.impl;

import io.appwish.profileservice.repository.ProfileRepository;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowIterator;

/**
 * Enables storing user profiles in PostgreSQL
 */
public class PostgresProfileRepository implements ProfileRepository {

  private static final String ID_COLUMN = "id";
  private static final String BIO_COLUMN = "bio";
  private static final String COVER_IMAGE_URL_COLUMN = "cover_image_url";
  private static final String GITHUB_USERNAME_COLUMN = "github_username";

  private final PgPool client;

  public PostgresProfileRepository(final PgPool client) {
    this.client = client;
  }

  @Override
  public Future<String> test() { // TODO remove test code when implementing storing profiles
    final Promise<String> promise = Promise.promise();

    client.preparedQuery(Query.FIND_ALL_PROFILE.sql(), query -> {
      final RowIterator<Row> iterator = query.result().iterator();
      if (query.succeeded() && iterator.hasNext()) {
        promise.complete(iterator.next().getString(BIO_COLUMN));
      } else if (query.succeeded()) {
        promise.complete("NOT FOUND ANY");
      } else {
        promise.fail(new RuntimeException());
      }
    });

    return promise.future();
  }
}
