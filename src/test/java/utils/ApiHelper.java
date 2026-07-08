package utils;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.AdData;
import models.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiHelper {

    static {
        RestAssured.baseURI = TestConfig.API_URL;
    }

    public static Response registerUser(User user) {
        System.out.println("Registering: " + user.getEmail());

        Map<String, String> userData = new HashMap<>();
        userData.put("email", user.getEmail());
        userData.put("password", user.getPassword());
        userData.put("submitPassword", user.getPassword());

        return given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post(TestConfig.REGISTER_ENDPOINT);
    }

    /**
     * Извлекает токен из ответа регистрации
     * Структура ответа: {"access_token": {"access_token": "..."}}
     */
    public static String extractTokenFromRegisterResponse(Response response) {
        try {
            // Пробуем получить access_token.access_token
            String token = response.jsonPath().getString("access_token.access_token");
            if (token != null) {
                System.out.println("Token extracted: " + token.substring(0, 30) + "...");
                return token;
            }
        } catch (Exception e) {
            System.out.println("Could not extract nested token: " + e.getMessage());
        }

        // Пробуем получить просто access_token
        String token = response.jsonPath().getString("access_token");
        if (token != null && !token.startsWith("{")) {
            System.out.println("Token extracted (flat): " + token.substring(0, 30) + "...");
            return token;
        }

        // Пробуем получить token
        token = response.jsonPath().getString("token");
        System.out.println("Token extracted (token): " + (token != null ? token.substring(0, 30) + "..." : "null"));
        return token;
    }

    public static Response loginUser(String email, String password) {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .when()
                .post(TestConfig.LOGIN_ENDPOINT);
    }

    public static String getAuthToken(String email, String password) {
        Response response = loginUser(email, password);
        return extractTokenFromRegisterResponse(response);
    }

    public static Response createAd(String token, AdData ad) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(ad)
                .when()
                .post(TestConfig.ITEMS_ENDPOINT);
    }

    public static String getAdIdFromResponse(Response response) {
        return response.jsonPath().getString("id");
    }
}