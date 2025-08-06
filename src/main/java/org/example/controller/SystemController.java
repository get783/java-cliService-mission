package org.example.controller;

import org.example.AppContext;

public class SystemController {
    public String scan() {
        return AppContext.scanner.nextLine().trim();
    }

    public void close() {
        AppContext.scanner.close();
    }
}
