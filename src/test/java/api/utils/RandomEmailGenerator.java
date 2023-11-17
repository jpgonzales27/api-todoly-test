package api.utils;

import java.util.Random;
public class RandomEmailGenerator {

    // Función para generar un string aleatorio de longitud dada
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            result.append(characters.charAt(randomIndex));
        }


        return result.toString();
    }

    public static String generateRandomUser(int length) {
        String randomEmail = "user" + generateRandomString(length) + "@example.com";
        return randomEmail;
    }
}
