package steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.TestConfig;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.awt.Toolkit;

public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        Configuration.baseUrl = TestConfig.BASE_URL;
        // Устанавливаем большой размер окна
        Configuration.browserSize = "1920x1080";

        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .screenshots(true)
                        .savePageSource(true)
        );

        Allure.getLifecycle().updateTestCase(testResult ->
                testResult.setName(scenario.getName()));

        // Сначала открываем любую страницу чтобы WebDriver создался
        Selenide.open(TestConfig.BASE_URL);

        // Теперь когда WebDriver точно есть - максимизируем
        try {
            WebDriver driver = WebDriverRunner.getWebDriver();
            driver.manage().window().maximize();

            // Получаем размер экрана и устанавливаем окно на весь экран
            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            driver.manage().window().setSize(new Dimension(screenSize.width, screenSize.height));

            System.out.println("Browser window maximized to: " + screenSize.width + "x" + screenSize.height);
        } catch (Exception e) {
            System.out.println("Could not maximize: " + e.getMessage());
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed() && WebDriverRunner.hasWebDriverStarted()) {
                byte[] screenshot = ((TakesScreenshot) WebDriverRunner.getWebDriver())
                        .getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
            }
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        } finally {
            try {
                if (WebDriverRunner.hasWebDriverStarted()) {
                    Selenide.clearBrowserCookies();
                    Selenide.closeWebDriver();
                }
            } catch (Exception e) {
                System.err.println("Error closing WebDriver: " + e.getMessage());
            }
        }
    }
}