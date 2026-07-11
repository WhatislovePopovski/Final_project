package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class MainPage extends BasePage {
    private final ElementsCollection adCards = $$x("//h2[@class='h2']");
    // Поле поиска
    private final SelenideElement searchInput = $x("/html/body/div/div/div[2]/form/div[1]/div/div/input");
    // Поле выбора категории для поиска
    private final SelenideElement searchCategoryInput = $x("/html/body/div/div/div[2]/form/div[2]/div[1]/div[1]/input");
    // Кнопка "Применить"
    private final SelenideElement applyButton = $x("//*[@id='root']/div/div[2]/form/div[2]/button");
    // Модальное окно
    private final SelenideElement modalOverlay = $("div.homePage_modal__zSdUB");
    private final SelenideElement modalCloseButton = $x("//div[contains(@class,'modal')]//button | //div[contains(@class,'modal')]//*[contains(@class,'close')]");

    public MainPage openPage() {
        com.codeborne.selenide.Selenide.open("/");
        waitForPageLoad();
        closeModalIfPresent();
        return this;
    }

    private void closeModalIfPresent() {
        sleep(500);
        if (modalOverlay.exists()) {
            System.out.println("Modal detected, closing...");
            if (modalCloseButton.exists()) {
                modalCloseButton.click();
            } else {
                modalOverlay.click();
            }
            sleep(500);
        }
    }

    /**
     * Поиск объявления по названию с учетом категории
     */
    public MainPage searchAd(String title, String category) {
        closeModalIfPresent();
        System.out.println("Searching for: " + title + " in category: " + category);

        // Выбираем категорию
        if (category != null && !category.isEmpty()) {
            // Кликаем по полю категории для открытия дропдауна
            SelenideElement categoryArrow = searchCategoryInput.parent().$("svg");
            if (categoryArrow.exists()) {
                categoryArrow.click();
            } else {
                searchCategoryInput.click();
            }
            sleep(500);

            // Выбираем нужную категорию из списка
            SelenideElement option = $x("//span[contains(@class,'dropDownMenu_textColor') and contains(text(),'" + category + "')]");
            if (option.exists()) {
                option.parent().click();
                sleep(300);
            }
        }

        // Вводим название в поле поиска
        searchInput.shouldBe(visible).clear();
        searchInput.setValue(title);
        sleep(300);

        // Нажимаем кнопку "Применить"
        applyButton.shouldBe(visible).click();
        sleep(1500); // Ждем результаты поиска

        return this;
    }

    /**
     * Поиск по названию (без категории)
     */
    public MainPage searchAd(String title) {
        return searchAd(title, null);
    }

    /**
     * Найти объявление по заголовку и кликнуть по нему
     */
    public void clickOnAdByTitle(String title) {
        closeModalIfPresent();
        // Ищем точный заголовок в карточке
        SelenideElement ad = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (!ad.exists()) {
            // Пробуем искать по части заголовка
            String shortTitle = title.length() > 10 ? title.substring(0, 10) : title;
            ad = $x("//h2[@class='h2' and contains(text(),'" + shortTitle + "')]");
        }
        if (!ad.exists()) {
            ad = $x("//*[contains(text(),'" + title.substring(0, Math.min(15, title.length())) + "')]");
        }
        ad.shouldBe(visible).click();
    }

    public boolean isAdPresent(String title) {
        SelenideElement ad = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (ad.exists()) return true;
        return $x("//h2[@class='h2' and contains(text(),'" + title + "')]").exists();
    }

    public SelenideElement getAdByTitle(String title) {
        SelenideElement titleElement = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (titleElement.exists()) {
            return titleElement.parent().parent().parent();
        }
        return $x("//h2[@class='h2' and contains(text(),'" + title + "')]");
    }

    public int getAdsCount() {
        return adCards.size();
    }

    public MainPage clickCreateAd() {
        closeModalIfPresent();
        createAdButton.shouldBe(visible).click();
        return this;
    }

    public MainPage waitForPageLoad() {
        $("body").shouldBe(visible);
        return this;
    }
}