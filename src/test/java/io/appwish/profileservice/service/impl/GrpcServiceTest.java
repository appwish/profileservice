package io.appwish.profileservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.grpc.Input;
import io.appwish.grpc.Output;
import io.appwish.profileservice.eventbus.Address;
import io.appwish.profileservice.eventbus.EventBusConfigurer;
import io.appwish.profileservice.service.GrpcServiceImpl;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class GrpcServiceTest {

  private GrpcServiceImpl grpcService;

  @BeforeEach
  void setUp(final Vertx vertx, final VertxTestContext context) {
    final EventBusConfigurer eventBusConfigurer = new EventBusConfigurer(vertx.eventBus());
    grpcService = new GrpcServiceImpl(vertx.eventBus());
    eventBusConfigurer.registerCodecs();
    context.completeNow();
  }

  @Test
  void should_return_all_wishes(final Vertx vertx, VertxTestContext context) {
    // given
    final Promise<Output> promise = Promise.promise();
    vertx.eventBus().consumer(Address.TEST_ADDRESS.get(), event -> {
      event.reply("BLABLA");
    });

    // when
    grpcService.test(Input.newBuilder().build(), promise);

    // then
    promise.future().setHandler(event -> {
      context.verify(() -> {
        assertTrue(promise.future().succeeded());
        assertEquals("BLABLA", promise.future().result().getOutput());
        context.completeNow();
      });
    });
  }
}
