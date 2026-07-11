package steps;

import io.cucumber.java.ru.*;
import models.User;
import pages.LoginPage;
import pages.MainPage;
import utils.ApiHelper;
import utils.TestDataGenerator;

import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.*;

public class LoginSteps {
    private LoginPage loginPage;
    private MainPage mainPage;
    private User currentUser;

    public LoginSteps() {
        this.loginPage = new LoginPage();
        this.mainPage = new MainPage();
    }

    @Допустим("пользователь зарегистрирован с email {string} и паролем {string}")
    public void registerUserViaApi(String email, String password) {
        currentUser = TestDataGenerator.generateRandomUser();
        currentUser.setEmail(email);
        currentUser.setPassword(password);

        var response = ApiHelper.registerUser(currentUser);
        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 201 || statusCode == 400,
                "API error. Status: " + statusCode);
    }

    @Когда("пользователь вводит email {string} и пароль {string}")
    public void enterLoginCredentials(String email, String password) {
        loginPage.openPage();
        sleep(1000);
        loginPage.fillEmail(email).fillPassword(password);
    }

    @Когда("пользователь нажимает кнопку \"Войти\"")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    @Тогда("пользователь успешно авторизован")
    public void verifyUserLoggedIn() {
        sleep(2000);
        assertTrue(mainPage.isUserLoggedIn(), "Пользователь не авторизован");
    }

    @Тогда("отображается главная страница с кнопкой создания объявления")
    public void verifyMainPageWithCreateButton() {
        assertTrue(mainPage.isUserLoggedIn(), "Кнопка создания объявления не отображается");
    }
}