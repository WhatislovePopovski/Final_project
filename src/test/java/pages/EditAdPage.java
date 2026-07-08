package pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class EditAdPage {
    private final SelenideElement titleInput = $("input[name='name']");
    private final SelenideElement descriptionInput = $("textarea[name='description']");
    private final SelenideElement priceInput = $("input[name='price']");
    private final SelenideElement submitButton = $x("//button[contains(text(),'Сохранить') or contains(text(),'Опубликовать')]");
    private final SelenideElement deleteButton = $x("//button[contains(text(),'Удалить')]");
    private final SelenideElement confirmDeleteButton = $x("//button[contains(text(),'Да') or contains(text(),'Подтвердить')]");

    public EditAdPage clearTitle() {
        titleInput.shouldBe(visible).clear();
        return this;
    }

    public EditAdPage fillTitle(String title) {
        titleInput.setValue(title);
        return this;
    }

    public EditAdPage clearPrice() {
        priceInput.shouldBe(visible).clear();
        return this;
    }

    public EditAdPage fillPrice(String price) {
        priceInput.setValue(price);
        return this;
    }

    public EditAdPage fillDescription(String description) {
        descriptionInput.shouldBe(visible).clear();
        descriptionInput.setValue(description);
        return this;
    }

    public EditAdPage clickSubmit() {
        submitButton.click();
        return this;
    }

    public EditAdPage clickDelete() {
        deleteButton.click();
        return this;
    }

    public EditAdPage confirmDelete() {
        sleep(500);
        if (confirmDeleteButton.exists()) {
            confirmDeleteButton.click();
        }
        return this;
    }

    public boolean isEditFormVisible() {
        return titleInput.exists();
    }
}