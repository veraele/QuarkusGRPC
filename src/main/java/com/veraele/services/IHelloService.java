package com.veraele.services;

import io.smallrye.mutiny.Uni;

public interface IHelloService {
    Uni<String> hello(String name);
}
