package config;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestConfig {
    public static final String BASE_URL = "https://qa-desk.education-services.ru";
    public static final String API_URL = BASE_URL + "/api";

    public static final long IMPLICIT_WAIT = 10;

    // API Endpoints
    public static final String REGISTER_ENDPOINT = "/signup";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String ITEMS_ENDPOINT = "/items";

    static {
        System.out.println("=== Инициализация TestConfig ===");
        System.out.println("BASE_URL: " + BASE_URL);

        WebDriverManager.chromedriver().setup();

        Configuration.baseUrl = BASE_URL;
        Configuration.timeout = IMPLICIT_WAIT * 1000;
        Configuration.browserSize = "1920x1080";  // Размер окна
        Configuration.headless = false;
        Configuration.savePageSource = true;
        Configuration.screenshots = true;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.pageLoadStrategy = "eager";
    }
}