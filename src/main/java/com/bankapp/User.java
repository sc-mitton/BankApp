package main.java.com.bankapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
  private String phone;
  private char[] pin;
  private char[] password;
  private ArrayList<Account> accounts = new ArrayList<>();

  public User(String firstName, String lastName, String userName, String email, String phone,
      char[] pin, char[] password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.userName = userName;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.pin = pin;
  }

  public User() {
  }

  // Get methods
  public String getName() {
    return firstName.concat(" " + lastName);
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getUsername() {
    return userName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public char[] getPin() {
    return pin;
  }

  public ArrayList<Account> getAccounts() {
    return accounts;
  }

  // Set methods
  public void setPassword(char[] password) {
    this.password = password;
  }

  public void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {

    Pattern p = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    Matcher m = p.matcher(email);

    if (m.matches()) {
      this.email = email;
    } else {
      throw new IllegalArgumentException("Entry isn't a valid email");
    }

  }

  public void setPhone(String phone) {

    if (phone.length() != 10) {
      throw new IllegalArgumentException("Entry isn't a valid phone number");
    } else {
      this.phone = phone;
    }

  }

  public void setUsername(String userName) {
    this.userName = userName;
  }

  public void addAccount(Account account) {
    accounts.add(account);
  }

  public void setPin(char[] pin) {
    this.pin = pin;
  }

  //
  public boolean verifyPassword(char[] password) {
    if (Arrays.equals(this.password, password)) {
      return true;
    } else {
      return false;
    }
  }
}