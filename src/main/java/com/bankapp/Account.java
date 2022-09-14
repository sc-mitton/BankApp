package main.java.com.bankapp;

import java.util.Random;

abstract class Account {
  private final String accountNumber;
  private int balance;
  private String name;

  public Account() {

    // Generate Account Number
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    sb.append(random.nextInt(9));
    for (int i = 0; i < 12; i++) {
      sb.append(random.nextInt(9) + 1);
    }
    accountNumber = sb.toString();

    // Start balance at 0 until a deposit is made
    balance = 0;
  }

  // Getters
  public String getAccountNumber() {
    return accountNumber;
  }

  public int getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  public String getName() {
    return name;
  }

  abstract public String getType();

  // Setters
  public void setName(String name) {
    this.name = name;
  }

  //
  abstract void makeDeposit(int amount);

  abstract void makeWithdrawal(int amount);
}
