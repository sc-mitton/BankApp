package com.bankapp;

import java.io.IOException;

import util.LoggingUtil;

// Driver program for the Bank application
public class Main {
  public static void main(String[] args) {

    Screen screen = Screen.getInstance();
    screen.run();
  }
}