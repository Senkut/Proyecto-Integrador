package edu.usta.infrastructure.config;

import java.util.UUID;

/**
 * Generador de identificadores únicos basados en el estándar UUID v4.
 * 
 * @author SenkuT
 */

public class UUIDGenerator {
    /**
     * Genera un identificador unico.
     * 
     * @return ID de tipo String en formato uuid v4.
     */

    public static String generate() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static UUID parseUUID(String id) {
        return UUID.fromString(id);
    }


}
