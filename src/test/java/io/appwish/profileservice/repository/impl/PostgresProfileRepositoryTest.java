package io.appwish.profileservice.repository.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.profileservice.repository.ProfileRepository;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class PostgresProfileRepositoryTest {

  private static final String DATABASE_HOST = "localhost";
  private static final String DEFAULT_POSTGRES = "postgres";

  private EmbeddedPostgres postgres;
  private ProfileRepository repository;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) throws Exception {
    postgres = EmbeddedPostgres.start();

    final PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(postgres.getPort())
      .setHost(DATABASE_HOST)
      .setDatabase(DEFAULT_POSTGRES)
      .setUser(DEFAULT_POSTGRES)
      .setPassword(DEFAULT_POSTGRES);
    final PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    final PgPool client = PgPool.pool(connectOptions, poolOptions);

    client.query(Query.CREATE_PROFILE_TABLE.sql(), context.completing());

    repository = new PostgresProfileRepository(client);
  }

  @AfterEach
  void tearDown() throws Exception {
    postgres.close();
  }

  @Test
  void should_interact_with_database_without_issues(final Vertx vertx, final VertxTestContext context) {
    // given

    // when
    repository.test().setHandler(event -> { // TODO remove test code when implementing storing profiles

        // then
        context.verify(() -> {
          assertTrue(event.succeeded());
          assertTrue(event.result().equals("NOT FOUND ANY"));
          context.completeNow();
        });
      });
  }
}
