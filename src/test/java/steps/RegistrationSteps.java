package steps;

import com.codeborne.selenide.WebDriverRunner;
import config.TestConfig;
import io.cucumber.java.ru.*;
import models.User;
import pages.RegistrationPage;
import utils.ApiHelper;
import utils.TestDataGenerator;

import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationSteps {
    private RegistrationPage registrationPage;
    private User currentUser;

    public RegistrationSteps() {
        this.registrationPage = new RegistrationPage();
    }

    @Допустим("пользователь находится на странице регистрации")
    public void openRegistrationPage() {
        registrationPage.openPage();
        assertTrue(registrationPage.isRegistrationFormVisible(),
                "Форма регистрации не отображается");
    }

    @Когда("пользователь вводит уникальный email и валидный пароль")
    public void enterUniqueEmailAndPassword() {
        currentUser = TestDataGenerator.generateRandomUser();
        registrationPage
                .fillEmail(currentUser.getEmail())
                .fillPassword(currentUser.getPassword())
                .fillConfirmPassword(currentUser.getPassword());
    }

    @Когда("пользователь нажимает кнопку \"Создать аккаунт\"")
    public void clickRegisterButton() {
        registrationPage.clickRegister();
    }

    @Тогда("пользователь перенаправлен на главную страницу")
    public void verifyRedirectedToMainPage() {
        // Ждем редиректа или появления кнопки "Разместить объявление"
        sleep(3000);

        // Проверяем, что мы авторизованы (есть кнопка "Разместить объявление")
        assertTrue(registrationPage.isUserLoggedIn(),
                "Пользователь не авторизован после регистрации. URL: " + WebDriverRunner.url());
    }

    @Допустим("пользователь с email {string} уже зарегистрирован")
    public void createExistingUser(String email) {
        currentUser = TestDataGenerator.generateRandomUser();
        currentUser.setEmail(email);

        var response = ApiHelper.registerUser(currentUser);
        int statusCode = response.getStatusCode();
        System.out.println("Create existing user status: " + statusCode);

        // 201 - создан, 400 - уже существует
        assertTrue(statusCode == 201 || statusCode == 400,
                "API error. Status: " + statusCode);
    }

    @Когда("пользователь вводит email {string} и пароль")
    public void enterExistingEmailAndPassword(String email) {
        // Открываем страницу регистрации заново
        registrationPage.openPage();
        registrationPage
                .fillEmail(email)
                .fillPassword(currentUser.getPassword())
                .fillConfirmPassword(currentUser.getPassword());
    }

    @Тогда("пользователь видит сообщение об ошибке {string}")
    public void verifyErrorMessage(String expectedError) {
        sleep(2000);

        // Проверяем наличие сообщения об ошибке
        if (registrationPage.isErrorMessageDisplayed()) {
            String actualError = registrationPage.getErrorSpanText();
            System.out.println("Error text: " + actualError);
            assertTrue(actualError.contains(expectedError) || expectedError.contains(actualError),
                    "Ожидалось: '" + expectedError + "', получено: '" + actualError + "'");
        } else {
            // Проверяем в page source
            String pageSource = WebDriverRunner.getWebDriver().getPageSource();
            assertTrue(pageSource.contains(expectedError),
                    "Сообщение об ошибке '" + expectedError + "' не найдено на странице");
        }
    }
}