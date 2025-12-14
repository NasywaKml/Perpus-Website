package Perpuskaan.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Integer idUser;
    private String username;
    private String email;
    private String role;

    // Constructor untuk memudahkan pengisian data
    public LoginResponse(String token, Integer idUser, String username, String email, String role) {
        this.token = token;
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}