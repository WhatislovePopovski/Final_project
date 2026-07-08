package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class RegistrationPage extends BasePage {
    private final SelenideElement emailInput = $("input[name='email']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement confirmPasswordInput = $("input[name='submitPassword']");
    // Кнопка "Создать аккаунт" по тексту
    private final SelenideElement registerButton = $x("//button[contains(text(),'Создать аккаунт')]");
    // Сообщение об ошибке
    private final SelenideElement errorSpan = $x("//span[contains(@class,'input_span')]");

    public RegistrationPage openPage() {
        com.codeborne.selenide.Selenide.open("/registration");
        waitForPageLoad();
        return this;
    }

    public RegistrationPage fillEmail(String email) {
        emailInput.shouldBe(visible).clear();
        emailInput.setValue(email);
        return this;
    }

    public RegistrationPage fillPassword(String password) {
        passwordInput.shouldBe(visible).clear();
        passwordInput.setValue(password);
        return this;
    }

    public RegistrationPage fillConfirmPassword(String password) {
        confirmPasswordInput.shouldBe(visible).clear();
        confirmPasswordInput.setValue(password);
        return this;
    }

    public RegistrationPage clickRegister() {
        registerButton.shouldBe(enabled).click();
        return this;
    }

    public boolean isRegistrationFormVisible() {
        return emailInput.isDisplayed();
    }

    public String getErrorSpanText() {
        return errorSpan.getText();
    }

    public RegistrationPage waitForPageLoad() {
        emailInput.shouldBe(visible);
        return this;
    }
}