package com.SAMProject.CarSharing.security;



import java.util.concurrent.ConcurrentHashMap;

public class TokenStorage {
    private static final ConcurrentHashMap<String, String> tokenUserMap = new ConcurrentHashMap<>();

    public static void storeToken(String token, String username) {
        tokenUserMap.put(token, username);
    }

    public static String getUsernameForToken(String token) {
        return tokenUserMap.get(token);
    }

    public static void removeToken(String token) {
        tokenUserMap.remove(token);
    }
}