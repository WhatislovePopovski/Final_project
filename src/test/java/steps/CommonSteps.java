package steps;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import config.TestConfig;
import io.cucumber.java.ru.*;
import pages.MainPage;
import static org.junit.jupiter.api.Assertions.*;

public class CommonSteps {
    private MainPage mainPage;

    public CommonSteps() {
        mainPage = new MainPage();
    }

    @Допустим("пользователь открывает главную страницу")
    public void openMainPage() {
        mainPage.openPage();  // Убедитесь что здесь openPage()
    }

    @Тогда("пользователь находится на главной странице")
    public void verifyOnMainPage() {
        String currentUrl = WebDriverRunner.url();
        assertTrue(currentUrl.contains(TestConfig.BASE_URL),
                "Пользователь не на главной странице");
    }

    @Тогда("отображается сообщение об ошибке {string}")
    public void verifyErrorMessage(String expectedMessage) {
        String actualMessage = mainPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                String.format("Ожидалось сообщение '%s', но получено '%s'",
                        expectedMessage, actualMessage));
    }

    @Тогда("отображается сообщение об успехе {string}")
    public void verifySuccessMessage(String expectedMessage) {
        String actualMessage = mainPage.getSuccessMessage();
        assertTrue(actualMessage.contains(expectedMessage),
                String.format("Ожидалось сообщение '%s', но получено '%s'",
                        expectedMessage, actualMessage));
    }
}