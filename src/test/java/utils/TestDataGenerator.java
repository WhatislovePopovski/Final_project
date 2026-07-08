package utils;

import com.github.javafaker.Faker;
import models.AdData;
import models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static String generateUniqueEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "test." + timestamp + "@example.com";
    }

    public static String generatePassword() {
        return "Test" + faker.number().digits(6) + "!A";
    }

    public static String generateName() {
        return faker.name().fullName();
    }

    public static String generatePhone() {
        return "+7" + faker.number().digits(10);
    }

    public static String generateAdTitle() {
        String[] prefixes = {"Новый", "Б/у", "Отличный", "Редкий"};
        String prefix = prefixes[faker.random().nextInt(prefixes.length)];
        return prefix + " " + faker.commerce().productName();
    }

    public static String generateAdDescription() {
        return faker.lorem().paragraph(3);
    }

    public static double generatePrice() {
        // Генерируем целое число без десятичных знаков
        return (double) faker.number().numberBetween(100, 100000);
    }

    public static String generateCategory() {
        String[] categories = {"Авто", "Книги", "Садоводство", "Хобби", "Технологии"};
        return categories[faker.random().nextInt(categories.length)];
    }

    public static String generateCity() {
        String[] cities = {"Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Нижний Новгород", "Казань"};
        return cities[faker.random().nextInt(cities.length)];
    }

    public static User generateRandomUser() {
        return User.builder()
                .email(generateUniqueEmail())
                .password(generatePassword())
                .name(generateName())
                .phone(generatePhone())
                .build();
    }

    public static AdData generateRandomAd() {
        return AdData.builder()
                .title(generateAdTitle())
                .description(generateAdDescription())
                .price(generatePrice())
                .category(generateCategory())
                .imagePath("src/test/resources/testdata/test-image.jpg")
                .build();
    }
}