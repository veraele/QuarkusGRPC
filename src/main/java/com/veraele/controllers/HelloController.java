package com.veraele.controllers;

import com.veraele.hello.grpc.Greeter;
import com.veraele.hello.grpc.HelloReply;
import com.veraele.hello.grpc.HelloRequest;
import com.veraele.services.IHelloService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@GrpcService
public class HelloController implements Greeter {
    @Inject
    IHelloService helloService;
    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return helloService.hello(request.getName())
                .onItem().ifNotNull()
                .transform( item -> HelloReply.newBuilder().setMessage(item).build());
    }
}
