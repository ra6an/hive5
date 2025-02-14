package com.hive5.hive5.component;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.UUID;

//@Component
//public class MyStompSessionHandler implements StompSessionHandler {
//    private final ActiveUserService activeUserService;
//
//    public MyStompSessionHandler(ActiveUserService activeUserService) {
//        this.activeUserService = activeUserService;
//    }
//
//    @Override
//    public void afterConnected(StompSession stompSession, StompHeaders connectedHeaders) {
//        // Prvo provjeravamo podatke iz connect headera
//        System.out.println(connectedHeaders);
//        String userIdStr = connectedHeaders.getFirst("userId");
//        System.out.println("USER ID: ======> " + userIdStr);
//
//        if (userIdStr != null) {
//            try {
//                UUID userId = UUID.fromString(userIdStr);
//                activeUserService.addUser(userId, stompSession.getSessionId());
//                System.out.println("User connected: " + userId + " --- SESSION ID: " + stompSession.getSessionId());
//            } catch (IllegalArgumentException e) {
//                System.out.println("Invalid UUID format: " + userIdStr);
//            }
//        } else {
//            System.out.println("userId nije pronađen u connect headerima");
//        }
//    }
//
//    @Override
//    public Type getPayloadType(StompHeaders stompHeaders) {
//        return byte[].class; // Ili tip koji očekuješ
//    }
//
//    @Override
//    public void handleFrame(StompHeaders stompHeaders, Object o) {
//        // Obrađivanje frame-ova
//    }
//
//    @Override
//    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
//        // Obrada grešaka
//        System.out.println("Greška pri obradi STOMP komande: " + stompCommand);
//        throwable.printStackTrace();
//    }
//
//    @Override
//    public void handleTransportError(StompSession stompSession, Throwable throwable) {
//        // Obrada transportnih grešaka
//        System.out.println("Greška u transportu: ");
//        throwable.printStackTrace();
//    }
//}
