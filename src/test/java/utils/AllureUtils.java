package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class AllureUtils {

    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot() {
        return ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "{attachmentName}", type = "text/plain")
    public static String attachText(String attachmentName, String text) {
        return text;
    }

    public static void addDescription(String description) {
        Allure.description(description);
    }

    public static void addParameter(String name, String value) {
        Allure.parameter(name, value);
    }
}