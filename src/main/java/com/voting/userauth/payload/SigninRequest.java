package com.voting.userauth.payload;

/**
 * @author michaelmak
 */
public class SigninRequest {

    private String username;
    private String password;
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public SigninRequest(String username, String password, String jwtToken) {
        this.username = username;
        this.password = password;
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
