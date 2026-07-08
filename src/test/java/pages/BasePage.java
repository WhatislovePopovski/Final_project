package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class BasePage {
    protected final SelenideElement loginAndRegisterButton = $x("//button[contains(text(),'Вход и регистрация')]");
    protected final SelenideElement createAdButton = $x("//button[contains(text(),'Разместить объявление')]");
    protected final SelenideElement successAlert = $x("//*[contains(@class,'success')]");
    protected final SelenideElement errorAlert = $x("//span[contains(@class,'input_span') and contains(text(),'Ошибка')]");

    public boolean isUserLoggedIn() {
        return createAdButton.exists();
    }

    public void goToLogin() {
        if (loginAndRegisterButton.exists()) {
            loginAndRegisterButton.click();
            sleep(1000);
        } else {
            com.codeborne.selenide.Selenide.open("/login");
        }
    }

    public void goToCreateAd() {
        createAdButton.shouldBe(visible).click();
    }

    public String getSuccessMessage() {
        return successAlert.shouldBe(visible).getText();
    }

    public String getErrorMessage() {
        return errorAlert.shouldBe(visible).getText();
    }

    public boolean isSuccessMessageDisplayed() {
        return successAlert.exists();
    }

    public boolean isErrorMessageDisplayed() {
        return errorAlert.exists();
    }
}