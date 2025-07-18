package com.outgoings.entity;

/*
Purpose of this class is to get user details during registration
process and pass it to Account Entity Object
 */
public class UserDTO {

  private String username;
  private String password;
  private String passwordRepeat;

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

  public String getPasswordRepeat() {
    return passwordRepeat;
  }

  public void setPasswordRepeat(String passwordRepeat) {
    this.passwordRepeat = passwordRepeat;
  }
}
