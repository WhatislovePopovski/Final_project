package models;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
    private String phone;

    public static User createDefault() {
        return User.builder()
                .email("test@example.com")
                .password("Test123!")
                .name("Test User")
                .phone("+79001234567")
                .build();
    }
}