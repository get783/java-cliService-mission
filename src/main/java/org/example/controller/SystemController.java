package org.example.controller;

import org.example.AppContext;

import java.util.Scanner;

public class SystemController {
    private final Scanner scanner;

    public SystemController() {
        this.scanner = AppContext.scanner;
    }

    public String scan() {
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }
}
