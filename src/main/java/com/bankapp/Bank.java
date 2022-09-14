package com.bankapp;

import java.util.HashMap;
import java.util.Map;

public class Bank {
  private Map<String, User> users = new HashMap<>();
  private Account[] accounts;
  private static Bank bank = null;

  private Bank() {
  }

  public static Bank getInstance() {
    if (bank == null) {
      bank = new Bank();
      return bank;
    } else {
      return bank;
    }
  }

  public void addUser(User newUser) {
    users.put(newUser.getUsername(), newUser);
  }

  public boolean verifyUser(String username) {
    return users.containsKey(username);
  }

  public boolean authenticateUser(String username, char[] password) {
    return users.get(username).verifyPassword(password);
  }

  public User getUser(String username) {
    return users.get(username);
  }

}
