package com.kaini.sList.utils;

import java.util.UUID;

/**
 * Genarate UUID
 */
public class UUIDGenerator {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}