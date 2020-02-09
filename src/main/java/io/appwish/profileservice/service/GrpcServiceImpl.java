package io.appwish.profileservice.service;

import io.appwish.grpc.Input;
import io.appwish.grpc.Output;
import io.appwish.grpc.ProfileServiceGrpc;
import io.appwish.profileservice.eventbus.Address;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import net.badata.protobuf.converter.Converter;

/**
 * Handles gRPC server request calls. Sends request on event bus to interact with wish data in the
 * database.
 */
public class GrpcServiceImpl extends ProfileServiceGrpc.ProfileServiceVertxImplBase {

  private final EventBus eventBus;
  private final Converter converter;

  public GrpcServiceImpl(final EventBus eventBus) {
    this.eventBus = eventBus;
    this.converter = Converter.create();
  }

  /**
   * This method gets invoked when other service (app, microservice) invokes stub.test()
   */
  @Override
  public void test(final Input request, final Promise<Output> response) { // TODO remove test code when implementing storing profiles
    eventBus.<String>request(Address.TEST_ADDRESS.get(), null, event -> {
      if (event.succeeded()) {
        response.complete(Output.newBuilder().setOutput(event.result().body()).build());
      } else if (event.succeeded()) {
        response.complete();
      } else {
        response.fail(event.cause());
      }
    });
  }
}
