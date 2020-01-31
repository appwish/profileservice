package io.appwish.profileservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.appwish.profileservice.eventbus.Address;
import io.appwish.profileservice.eventbus.EventBusConfigurer;
import io.appwish.profileservice.repository.ProfileRepository;
import io.appwish.profileservice.service.DatabaseService;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class DatabaseServiceTest {

  private io.appwish.profileservice.service.DatabaseService databaseService;
  private ProfileRepository profileRepository;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) {
    final EventBusConfigurer util = new EventBusConfigurer(vertx.eventBus());
    profileRepository = mock(ProfileRepository.class);
    databaseService = new DatabaseService(vertx.eventBus(), profileRepository);
    databaseService.registerEventBusEventHandlers();
    util.registerCodecs();
    context.completeNow();
  }

  @Test
  void simply_tests_if_service_invokes_repository_correctly(final Vertx vertx, final VertxTestContext context) {
    // given
    when(profileRepository.test()).thenReturn(Future.succeededFuture("BLABLA")); // TODO remove test code when implementing storing profiles

    // when
    vertx.eventBus().<String>request(Address.TEST_ADDRESS.get(), null, event -> {

        // then
        context.verify(() -> {
          assertEquals("BLABLA", event.result().body());
          context.completeNow();
        });
      });
  }
}
