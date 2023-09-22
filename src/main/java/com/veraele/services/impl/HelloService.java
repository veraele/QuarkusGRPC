package com.veraele.services.impl;

import com.veraele.services.IHelloService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HelloService implements IHelloService {

    @Override
    public Uni<String> hello(String name) {
        return Uni.createFrom().item("Hello "+name);
    }
}
