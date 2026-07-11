package models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdData {
    private String id;
    private String title;
    private String description;
    private double price;
    private String category;
    private String imagePath;
    private String city;
    private String condition;

    public static AdData createDefault() {
        return AdData.builder()
                .title("Test Product")
                .description("Test Description")
                .price(999.99)
                .category("Авто")
                .imagePath("src/test/resources/testdata/test-image.jpg")
                .city("Москва")
                .condition("Б/У")
                .build();
    }
}