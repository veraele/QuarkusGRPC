package com.veraele.controllers;

import com.veraele.chat.grpc.*;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.MultiEmitterProcessor;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.smallrye.mutiny.subscription.MultiEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class ChatController implements ChatService {

    private final Map<String, MultiEmitterProcessor<ChatMessage>> chatUsers = new ConcurrentHashMap<>();


    @Override
    public Uni<Empty> sendMessage(ChatMessage request) {
        broadcastMessage(request);
        return Uni.createFrom().item(Empty.newBuilder().build());
    }

    @Override
    public Multi<ChatMessage> joinChat(JoinRequest request) {
        String username = request.getUsername();
        MultiEmitterProcessor<ChatMessage> userProcessor = MultiEmitterProcessor.create();
        chatUsers.put(username, userProcessor);
        ChatMessage welcomeMessage = ChatMessage.newBuilder()
                .setUsername("Chat Bot")
                .setMessage(username + " se ha unido al chat.")
                .build();

        // Notificar a otros usuarios que un nuevo usuario se ha unido al chat
        broadcastMessage(welcomeMessage);

        // Devolver el Multi generado a partir del EmitterProcessor
        return Multi.createFrom().publisher(userProcessor);
    }

    private void broadcastMessage(ChatMessage message) {
        chatUsers.forEach((key, value) -> {
            value.onNext(message);
        });
    }
}
