package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class LoginPage extends BasePage {
    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    // Кнопка "Войти" по тексту
    private final SelenideElement loginButton = $x("//button[contains(text(),'Войти')]");

    public LoginPage openPage() {
        com.codeborne.selenide.Selenide.open("/login");
        waitForPageLoad();
        return this;
    }

    public LoginPage fillEmail(String email) {
        emailInput.shouldBe(visible).clear();
        emailInput.setValue(email);
        return this;
    }

    public LoginPage fillPassword(String password) {
        passwordInput.shouldBe(visible).clear();
        passwordInput.setValue(password);
        return this;
    }

    public void clickLogin() {
        loginButton.shouldBe(enabled).click();
    }

    public boolean isLoginFormVisible() {
        return emailInput.isDisplayed();
    }

    public LoginPage waitForPageLoad() {
        emailInput.shouldBe(visible);
        return this;
    }
}