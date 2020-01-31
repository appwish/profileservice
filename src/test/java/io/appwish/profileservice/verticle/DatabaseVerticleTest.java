package io.appwish.profileservice.verticle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.appwish.profileservice.eventbus.Address;
import io.appwish.profileservice.eventbus.EventBusConfigurer;
import io.appwish.profileservice.repository.ProfileRepository;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class DatabaseVerticleTest {

  @Test
  void should_expose_database_service(final Vertx vertx, final VertxTestContext context) {
    // given
    final ProfileRepository repository = mock(ProfileRepository.class);
    final DatabaseVerticle verticle = new DatabaseVerticle(repository);
    final EventBusConfigurer util = new EventBusConfigurer(vertx.eventBus());
    when(repository.test()).thenReturn(Future.succeededFuture("BLABLA")); // TODO remove test code when implementing storing profiles

    util.registerCodecs();

    // when
    vertx.deployVerticle(verticle, new DeploymentOptions(), context.succeeding());

    vertx.eventBus().request(Address.TEST_ADDRESS.get(), null, event -> { // TODO remove test code when implementing storing profiles
      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        assertEquals("BLABLA", event.result().body());
        context.completeNow();
      });

    });
  }
}
