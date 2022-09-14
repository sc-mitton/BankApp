package com.bankapp;

import java.time.MonthDay;

class SavingsAccount extends Account {
  int withdrawalCount = 0;
  private final String type = "savings";

  MonthDay lastWithdrawal = MonthDay.now();

  public SavingsAccount(String name) {
    super();
    super.setName(name);
  }

  public SavingsAccount(User user) {
    super();
    super.setName(user.getFirstName() + "'s Savings");
  }

  public String getType() {
    return this.type;
  }

  public void makeWithdrawal(int amount) {

    // If the DayofMonth of the current withdrawl is less than the DayofMonth of the
    // last withdrawl
    // then reset the withdrawal counter since the rest of the month has gone by
    // since the last withdrawal
    if (MonthDay.now().getDayOfMonth() < lastWithdrawal.getDayOfMonth()) {
      withdrawalCount = 0;
    }

    if (withdrawalCount < 6) {
      // Withdraw the money by subtracting the withdrawal amount
      int newBalance = super.getBalance() - amount;
      super.setBalance(newBalance);
      withdrawalCount++;
    } else {
      // Fee for withdrawing too much from savings
      int newBalance = super.getBalance() - amount - 35;
      super.setBalance(newBalance);
      withdrawalCount++;
    }

    lastWithdrawal = MonthDay.now(); // Update last withdrawal

  }

  public void makeDeposit(int amount) {
    int newBalance = super.getBalance() + amount;
    super.setBalance(newBalance);
  }
}