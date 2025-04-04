package ru.praktikumservices.qascooter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstname;

    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
