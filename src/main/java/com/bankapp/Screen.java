package com.bankapp;

import java.io.Console;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Screen {
  private static Screen screen;
  private Bank bank;
  private User currentUser;
  private Account selectedAccount;
  private int headerSize = 44;
  Console console = System.console();

  private enum ScreenState {
    WELCOME,
    LOGIN,
    CREATEUSER,
    MAINMENU,
    MAKEDEPOSIT,
    MAKEWITHDRAWAL,
    OPENACCOUNT,
    UNPDATEINFO,
  }

  ScreenState state; // app state

  private Screen() {
    // Start with an initial user
    User spencer = new User(
        "Spencer", "Mitton",
        "spencermitton", "smitton.byu@gmail.com",
        "9072012419", "0000".toCharArray(), "Arepa2021".toCharArray());
    state = ScreenState.WELCOME; // start at welcome page

    // Bank singleton
    bank = Bank.getInstance();
    bank.addUser(spencer);
  }

  public static Screen getInstance() {

    if (screen == null) {
      screen = new Screen();
    }

    return screen;
  }

  public void run() {
    do {
      switch (state) {
        case WELCOME:
          welcomePage();
          break;
        case LOGIN:
          login();
          break;
        case CREATEUSER:
          createUser();
          break;
        case MAINMENU:
          mainMenu();
          break;
        case MAKEDEPOSIT:
          makeDeposit(selectedAccount);
          break;
        case MAKEWITHDRAWAL:
          makeWithdrawal(selectedAccount);
          break;
        case OPENACCOUNT:
          openAccount();
          break;
        case UNPDATEINFO:
          updateInfo();
          break;
        default:
          welcomePage();
      }
    } while (true);

  }

  /* -------------------------------------------------------------------------- */
  /* Menu Pages */
  /* -------------------------------------------------------------------------- */
  private void welcomePage() {

    // Setup
    ConsoleManipulate.clearAll();
    if (console == null) {
      System.err.println("No console.");
      System.exit(1);
    }

    //
    String header = " Welcome to the Bank of Gondor ";
    String loginPrompt = "(1) Login";
    String registerPrompt = "(2) Register\n";

    // Screen
    System.out.println("=".repeat(headerSize / 2 - header.length() / 2) + header
        + "=".repeat(headerSize / 2 - header.length() / 2) + "\n");

    System.out.println(" ".repeat(headerSize / 2 - loginPrompt.length() / 2) + loginPrompt
        + " ".repeat(headerSize / 2 - loginPrompt.length() / 2));
    System.out.println(" ".repeat(headerSize / 2 - registerPrompt.length() / 2) + registerPrompt
        + " ".repeat(headerSize / 2 - registerPrompt.length() / 2));
    System.out.print(">>");

    boolean quit = false;
    do {
      // Select option
      String opCode = console.readLine();

      switch (opCode) {
        case ("1"):
          quit = true;
          state = ScreenState.LOGIN;
          break;
        case ("2"):
          quit = true;
          state = ScreenState.CREATEUSER;
          break;
        default:
          ConsoleManipulate.clearLines(1);
          System.out.print("Please enter one of the numbered options >>");
      }
    } while (!quit);

  }

  private void login() {

    if (console == null) {
      System.err.println("No console.");
      System.exit(1);
    }

    // Login Prompt Content
    String userNamePrompt = "\nUser Name:";
    String passwordPrompt = "Password:\n";
    String header = " Login ";

    do {
      ConsoleManipulate.clearAll();

      // Login Prompt
      printHeader(header);
      System.out.println(userNamePrompt);
      System.out.println(passwordPrompt);

      // Get the username and password
      ConsoleManipulate.moveTo(3, userNamePrompt.length());
      String userName = console.readLine().strip();
      if (userName.equals("b")) {
        System.out.print("\n");
        state = ScreenState.WELCOME;
        break;
      }
      ConsoleManipulate.moveRight(passwordPrompt.length());
      char[] password = console.readPassword();

      // Verify the user exists and the password matches
      if (bank.verifyUser(userName) && (bank.authenticateUser(userName, password))) {
        Arrays.fill(password, ' '); // Erase the password that was stored for security
        currentUser = bank.getUser(userName);
        System.out.printf("\nWelcome Back %s!", currentUser.getName());
        state = ScreenState.MAINMENU;
        break;
      } else {
        System.out.println("\nIncorrect username/password");
        consolePause(2000);
      }

    } while (true);

    consolePause(2000);

  }

  // Transfer logic for setting user fields to the User class setters
  private void createUser() {

    // Setup
    ConsoleManipulate.clearAll();
    if (console == null) {
      System.err.println("No console.");
      System.exit(1);
    }
    String header = " Register ";
    printHeader(header);

    User newUser = new User();

    // Read in the information to create a new user (escape flow if b is pressed)

    // First Name
    System.out.print("\nFirst Name: ");
    String s1 = console.readLine();
    if (s1.equals("b")) {
      state = ScreenState.WELCOME;
      return;
    }
    newUser.setFirstName(s1);

    // Last Name
    System.out.print("Last Name: ");
    String s2 = console.readLine();
    if (s2.equals("b")) {
      state = ScreenState.WELCOME;
      return;
    }
    newUser.setLastName(s2);

    // User Name
    System.out.print("User Name: ");
    String s3 = console.readLine();
    if (s3.equals("b")) {
      state = ScreenState.WELCOME;
      return;
    }
    newUser.setUsername(s3);

    // Email
    String s4;
    do {

      System.out.print("Email: ");
      s4 = console.readLine();
      if (s4.equals("b")) {
        state = ScreenState.WELCOME;
        return;
      }
      try {
        newUser.setEmail(s4);
        break;
      } catch (IllegalArgumentException e) {
        System.out.printf("%s\n", e.getMessage());
        consolePause(2000);
        ConsoleManipulate.clearLines(3);
      }
    } while (true);

    // Phone Number
    String s5;
    do {
      System.out.print("Phone: ");
      s5 = console.readLine();
      if (s5.equals("b")) {
        state = ScreenState.WELCOME;
        return;
      }
      try {
        newUser.setPhone(s5);
        break;
      } catch (IllegalArgumentException e) {
        System.out.printf("%s\n", e.getMessage());
        consolePause(2000);
        ConsoleManipulate.clearLines(3);
      }
    } while (true);

    // Pin
    char[] p1;
    do {
      System.out.print("Four Digit Pin: ");
      p1 = console.readPassword();
      if (p1[0] == 'b') {
        state = ScreenState.WELCOME;
        return;
      }
      if (p1.length != 4) {
        System.out.println("\nPin must be 4 digits");
        consolePause(2000);
        ConsoleManipulate.clearLines(3);
      } else {
        break;
      }
    } while (true);

    // Read in password
    char[] p2;
    char[] p3;
    do {
      System.out.print("Password: ");
      p2 = console.readPassword();
      if (p2[0] == 'b') {
        state = ScreenState.WELCOME;
        return;
      }
      System.out.print("Confirm Password: ");
      p3 = console.readPassword();

      if (Arrays.equals(p2, p3)) {
        newUser.setPassword(p3);
        Arrays.fill(p1, ' ');
        Arrays.fill(p2, ' ');
        Arrays.fill(p3, ' ');
        break;
      } else {
        ConsoleManipulate.clearLines(2);
        System.out.println("\nPasswords did not match");
        consolePause(2000);
        ConsoleManipulate.clearLines(2);
      }
    } while (true);

    bank.addUser(newUser);
    currentUser = newUser;
    state = ScreenState.MAINMENU;

  }

  private void mainMenu() {
    if (console == null) {
      System.err.println("No console.");
      System.exit(1);
    }
    String header = " Main Menu ";
    printHeader(header);

    for (Account account : currentUser.getAccounts()) {
      System.out.printf("\n%s Balance: $%s\n", account.getName(),
          (double) account.getBalance() / 100);
    }

    System.out.println("\n(1) Make a Deposit");
    System.out.println("(2) Make a Withdrawal");
    System.out.println("(3) Open Account");
    System.out.println("(4) Update Info");
    System.out.print("\n>>");

    String opcode = console.readLine().toLowerCase();

    // User selects the menu option
    boolean quit = false;
    do {
      switch (opcode) {
        case ("1"):
          printHeader(header);
          int i = 1;

          // Check if the user even has any accounts
          if (currentUser.getAccounts().size() == 0) {
            System.out.println("\nYou have no accounts");
            consolePause(2500);
            ConsoleManipulate.clearLines(1);
          } else {
            // Print out accounts
            for (Account account : currentUser.getAccounts()) {
              System.out.printf("\n(%d) %s\n", i, account.getName());
              i++;
            }

            // Make Selection
            do {
              System.out.print("\nSelect Account >> ");
              String selectedIndex = console.readLine();
              try {
                selectedAccount = currentUser.getAccounts().get(Integer.parseInt(
                    selectedIndex) - 1);
              } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a valid selection");
                consolePause(2000);
                ConsoleManipulate.clearLines(3);
              } catch (IndexOutOfBoundsException e) {
                System.out.println("\nPlease enter a valid number");
                consolePause(2000);
                ConsoleManipulate.clearLines(3);
              }

              state = ScreenState.MAKEDEPOSIT;
              break;
            } while (true);
          }

          quit = true;
          break;
        case ("2"):
          printHeader(header);
          int j = 1;

          // Check if the user even has any accounts
          if (currentUser.getAccounts().size() == 0) {
            System.out.println("\nYou have no accounts");
            consolePause(2500);
            ConsoleManipulate.clearLines(1);
          } else {
            // Print out accounts
            for (Account account : currentUser.getAccounts()) {
              System.out.printf("\n(%d) %s", j, account.getName());
              j++;
            }

            // Make Selection
            do {
              System.out.print("\nSelect Account >> ");
              String selectedIndex = console.readLine();
              try {
                selectedAccount = currentUser.getAccounts().get(Integer.parseInt(
                    selectedIndex) - 1);
              } catch (NumberFormatException e) {
                System.out.println("\nPlease enter a valid selection");
                consolePause(2000);
                ConsoleManipulate.clearLines(3);
              } catch (IndexOutOfBoundsException e) {
                System.out.println("\nPlease enter a valid number");
                consolePause(2000);
                ConsoleManipulate.clearLines(3);
              }

              state = ScreenState.MAKEWITHDRAWAL;
              break;
            } while (true);
          }

          quit = true;
          break;
        case ("3"):
          state = ScreenState.OPENACCOUNT;
          quit = true;
          break;
        case ("4"):
          state = ScreenState.UNPDATEINFO;
          quit = true;
          break;
        case ("b"):
          state = ScreenState.WELCOME;
          quit = true;
          break;
        default:
          System.out.println("Please enter a valid option");
          consolePause(2000);
          ConsoleManipulate.clearLines(2);
      }
    } while (!quit);

  }

  private void openAccount() {
    if (console == null) {
      System.err.println("No console.");
      System.exit(1);
    }
    String header = " Open Account ";
    printHeader(header);

    boolean quit = false;
    do {
      // Clear the console and provide options for new account
      System.out.print("\nWhat type of account would you like to open today?\n"
          + "(1) Checking Account\n"
          + "(2) Savings Account\n"
          + "\n>>");

      String selection = console.readLine();
      switch (selection) {
        case ("1"): {
          ConsoleManipulate.clearLines(1);
          System.out.print("Account Name: ");
          String accountName = console.readLine();

          if (accountName.equals("")) {
            CheckingAccount account = new CheckingAccount(currentUser);
            currentUser.addAccount(account);
          } else {
            CheckingAccount account = new CheckingAccount(accountName);
            currentUser.addAccount(account);
          }

          quit = true;
          break;
        }
        case ("2"): {
          ConsoleManipulate.clearLines(1);
          System.out.print("Account Name: ");
          String accountName = console.readLine();

          if (accountName.equals("")) {
            SavingsAccount account = new SavingsAccount(currentUser);
            currentUser.addAccount(account);
          } else {
            SavingsAccount account = new SavingsAccount(accountName);
            currentUser.addAccount(account);
          }

          quit = true;
          break;
        }
        case ("b"): {
          quit = true;
          break;
        }
        default: {
          System.out.println("Please enter either 1 or 2.");
        }
      }
    } while (!quit);

    state = ScreenState.MAINMENU;
  }

  private void makeDeposit(Account account) {
    // Setup
    if (console == null) {
      System.err.println("No Console.");
      System.exit(1);
    }
    String header = " Make Deposit ";

    // While loop for reading input
    do {
      printHeader(header);
      System.out.printf("\n%s Balance $ %.2f\n",
          account.getName(), ((double) account.getBalance()) / 100);
      System.out.print("Insert Cash ($5 minimum) >> ");

      Pattern p = Pattern.compile("^\\d+?\\.?\\d{2}$");
      String deposit = console.readLine();
      Matcher m = p.matcher(deposit);

      if (deposit.equals("b")) {
        break;
      }

      if (m.matches()) {
        // Minimum deposit requirement
        int d = (int) (Double.parseDouble(deposit) * 100);
        if (d >= 500) {
          account.makeDeposit(d);
        } else {
          System.out.println("\nDeposit must be at least $5");
          consolePause(2000);
        }
      }

    } while (true);

    // Farewell, cleanup, and return to previous screen
    ConsoleManipulate.clearLines(3);
    System.out.printf("\n%s Balance $ %.2f\n",
        account.getName(), ((double) account.getBalance()) / 100);

    consolePause(2000);
    state = ScreenState.MAINMENU;

  }

  private void makeWithdrawal(Account account) {
    // Setup
    if (console == null) {
      System.err.println("No Console.");
      System.exit(1);
    }
    String header = " Make Withdrawal ";

    // While loop for reading input
    do {
      printHeader(header);
      System.out.printf("\n%s Balance $ %.2f\n",
          account.getName(), ((double) account.getBalance()) / 100);
      System.out.print("Select Withdrawal Amount ($10 minimum) >> ");

      Pattern p = Pattern.compile("^\\d+?\\.?\\d{2}$");
      String withdrawal = console.readLine();
      Matcher m = p.matcher(withdrawal);

      if (withdrawal.equals("b")) {
        break;
      }

      if (m.matches()) {
        // Minimum deposit requirement
        int w = (int) (Double.parseDouble(withdrawal) * 100);
        if (w >= 1000) {
          account.makeWithdrawal(w);
        } else {
          System.out.println("\nDeposit must be at least $5");
          consolePause(2000);
        }
      }

    } while (true);

    // Farewell, cleanup, and return to previous screen
    ConsoleManipulate.clearLines(3);
    System.out.printf("\n%s Balance $ %.2f\n",
        account.getName(), ((double) account.getBalance()) / 100);

    consolePause(2000);
    state = ScreenState.MAINMENU;

  }

  // Transfer logic for setting user fields to the User class setters
  private void updateInfo() {

    // Start a new scanner and screen
    if (console == null) {
      System.err.println("No Console.");
      System.exit(1);
    }
    String header = " Update Info ";

    do {
      printHeader(header);
      printUserInfo();
      System.out.print(">>");

      String opcode = console.readLine().toLowerCase();

      switch (opcode) {
        case ("b"): {
          break;
        }
        case ("1"): {
          ConsoleManipulate.clearLines(1);
          System.out.print("First Name: ");
          String newFirstName = console.readLine();
          currentUser.setFirstName(newFirstName);

          ConsoleManipulate.clearLines(1);
          System.out.print("Last Name: ");
          String newLastName = console.readLine();
          currentUser.setLastName(newLastName);
          break;
        }
        case ("2"): {
          ConsoleManipulate.clearLines(1);
          System.out.print("User Name: ");
          String newUserName = console.readLine();
          currentUser.setUsername(newUserName);
          break;
        }
        case ("3"): {
          do {
            ConsoleManipulate.clearLines(1);
            System.out.print("Phone Number: ");
            String newPhone = console.readLine();
            try {
              currentUser.setPhone(newPhone);
              break;
            } catch (IllegalArgumentException e) {
              System.out.printf("\n%s\n", e.getMessage());
              consolePause(2000);
              ConsoleManipulate.clearLines(2);
            }
          } while (true);
          break;
        }
        case ("4"): {
          do {
            ConsoleManipulate.clearLines(1);
            System.out.print("Email: ");
            String newEmail = console.readLine();
            if (newEmail.equals("b")) {
              break;
            }
            try {
              currentUser.setEmail(newEmail);
              break;
            } catch (IllegalArgumentException e) {
              System.out.printf("\n%s\n", e.getMessage());
              consolePause(2000);
              ConsoleManipulate.clearLines(2);
            }
          } while (true);
          break;
        }
        default: {
          ConsoleManipulate.clearLines(1);
          System.out.print("Please enter a valid option >>");
        }
      }

      printHeader(header);
      printUserInfo();

      System.out.print("Would you like to update any other info? (y/n) ");
      String confirmation = console.readLine().toLowerCase();

      if (confirmation.equals("n")) {
        break;
      }

    } while (true);

    state = ScreenState.MAINMENU;
  }

  /* -------------------------------------------------------------------------- */
  /* Utils */
  /* -------------------------------------------------------------------------- */
  private void printHeader(String header) {
    ConsoleManipulate.clearAll();
    System.out.printf(
        "%s%s%s", "=".repeat(headerSize / 2 - header.length() / 2), header,
        "=".repeat(headerSize / 2 - header.length() / 2));
    System.out.println("\n<< (b)");
  }

  private void printUserInfo() {
    System.out.printf("\n(1) %s\n"
        + "(2) User Name: %s\n"
        + "(3) Phone Number: %s\n"
        + "(4) Email Address: %s\n\n",
        currentUser.getName(),
        currentUser.getUsername(),
        currentUser.getPhone(),
        currentUser.getEmail()); // Print personal info
  }

  private void consolePause(int snooze) {
    try {
      Thread.sleep(snooze);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
