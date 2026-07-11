package pages;

import com.codeborne.selenide.SelenideElement;
import java.io.File;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class CreateAdPage extends BasePage {
    private final SelenideElement titleInput = $("input[name='name']");
    private final SelenideElement descriptionInput = $("textarea[name='description']");
    private final SelenideElement priceInput = $("input[name='price']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement cityInput = $("input[name='city']");
    private final SelenideElement imageUpload = $("input[type='file']");
    private final SelenideElement conditionUsed = $x("//input[@name='condition' and @value='Б/У']");
    private final SelenideElement conditionNew = $x("//input[@name='condition' and @value='Новый']");
    // Точный XPath кнопки
    private final SelenideElement submitButton = $x("/html/body/div[1]/div/div[2]/div/form/button");

    public CreateAdPage fillTitle(String title) {
        titleInput.shouldBe(visible).clear();
        titleInput.setValue(title);
        return this;
    }

    public CreateAdPage fillDescription(String description) {
        descriptionInput.shouldBe(visible).clear();
        descriptionInput.setValue(description);
        return this;
    }

    public CreateAdPage fillPrice(String price) {
        // Убираем десятичную часть, оставляем только целое число
        String cleanPrice = price.contains(".") ? price.substring(0, price.indexOf(".")) : price;
        priceInput.shouldBe(visible).clear();
        priceInput.setValue(cleanPrice);
        return this;
    }

    public CreateAdPage selectCategory(String category) {
        SelenideElement categoryArrow = categoryInput.parent().$("svg");
        if (categoryArrow.exists()) {
            categoryArrow.click();
        } else {
            categoryInput.click();
        }
        sleep(500);

        SelenideElement option = $x("//span[contains(@class,'dropDownMenu_textColor') and contains(text(),'" + category + "')]");
        if (option.exists()) {
            SelenideElement parentOption = option.parent();
            parentOption.click();
        }
        return this;
    }

    public CreateAdPage selectCity(String city) {
        SelenideElement cityArrow = cityInput.parent().$("svg");
        if (cityArrow.exists()) {
            cityArrow.click();
        } else {
            cityInput.click();
        }
        sleep(500);

        SelenideElement option = $x("//button[contains(@class,'dropDownMenu_btn')]//span[contains(text(),'" + city + "')]");
        if (option.exists()) {
            option.parent().click();
        }
        return this;
    }

    public CreateAdPage selectCondition(String condition) {
        if (condition.equalsIgnoreCase("Новый")) {
            conditionNew.parent().click();
        } else {
            conditionUsed.parent().click();
        }
        return this;
    }

    public CreateAdPage uploadImage(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            imageUpload.uploadFile(imageFile);
        }
        return this;
    }

    public CreateAdPage clickSubmit() {
        // Проверяем состояние кнопки
        System.out.println("=== Submit button debug ===");
        System.out.println("Exists: " + submitButton.exists());
        System.out.println("Displayed: " + submitButton.isDisplayed());
        System.out.println("Enabled: " + submitButton.isEnabled());
        System.out.println("Text: " + submitButton.getText());
        System.out.println("Location: " + submitButton.getLocation());
        System.out.println("Size: " + submitButton.getSize());

        // Скроллим до кнопки
        submitButton.scrollIntoView(true);
        sleep(500);

        // Проверяем еще раз после скролла
        System.out.println("After scroll - Displayed: " + submitButton.isDisplayed());
        System.out.println("After scroll - Enabled: " + submitButton.isEnabled());

        // Пробуем кликнуть
        try {
            submitButton.click();
            System.out.println("Click successful!");
        } catch (Exception e) {
            System.out.println("Click failed: " + e.getMessage());
            // Пробуем через JavaScript
            executeJavaScript("arguments[0].click();", submitButton);
            System.out.println("JavaScript click executed");
        }

        sleep(2000);
        return this;
    }

    public CreateAdPage clearTitle() {
        titleInput.shouldBe(visible).clear();
        return this;
    }

    public CreateAdPage clearPrice() {
        priceInput.shouldBe(visible).clear();
        return this;
    }

    public boolean isCreateAdFormVisible() {
        return titleInput.exists();
    }
}