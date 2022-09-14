package main.java.com.bankapp;

public class CheckingAccount extends Account {
  private final String type = "checking";

  public CheckingAccount(String name) {
    super();
    super.setName(name);
  }

  public CheckingAccount(User user) {
    super();
    super.setName(user.getFirstName() + "'s Savings");
  }

  public String getType() {
    return this.type;
  }

  public void makeWithdrawal(int amount) {
    int newBalance = super.getBalance() - amount; // Fee for withdrawing too much from savings
    if (newBalance < 0) {
      newBalance -= 35; // Overdraft fee
    }
    super.setBalance(newBalance);
  }

  public void makeDeposit(int amount) {
    int newBalance = super.getBalance() + amount;
    super.setBalance(newBalance);
  }

  public void otherMethod() {
    System.out.println("Using other method");
  }

}
