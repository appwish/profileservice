package io.appwish.profileservice.verticle;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.appwish.grpc.Input;
import io.appwish.grpc.ProfileServiceGrpc.ProfileServiceVertxStub;
import io.appwish.profileservice.TestData;
import io.appwish.profileservice.eventbus.Address;
import io.appwish.profileservice.eventbus.EventBusConfigurer;
import io.grpc.ManagedChannel;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class GrpcVerticleTest {

  @Test
  void should_expose_grpc_server(final Vertx vertx, final VertxTestContext context) {
    // given
    final EventBusConfigurer util = new EventBusConfigurer(vertx.eventBus());
    final ManagedChannel channel = VertxChannelBuilder.forAddress(vertx, TestData.APP_HOST, TestData.APP_PORT).usePlaintext(true).build();
    final ProfileServiceVertxStub serviceStub = new ProfileServiceVertxStub(channel);
    vertx.deployVerticle(new GrpcVerticle(), new DeploymentOptions(), context.completing());
    vertx.eventBus().consumer(Address.TEST_ADDRESS.get(), event -> event.reply("TEST")); // TODO remove test code when implementing storing profiles

    util.registerCodecs();

    // when
    serviceStub.test(Input.newBuilder().build(), event -> {

      // then
      context.verify(() -> {
        assertTrue(event.succeeded());
        context.completeNow();
      });
    });
  }
}
