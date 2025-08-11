package org.example;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Request {
    private final String actionName;
    private final Map<String, String> paramsMap;

    Request(String command) {
        String[] cmdBits = command.split("\\?", 2);
        actionName = cmdBits[0].trim();
        String queryString = cmdBits.length == 2 ? cmdBits[1].trim() : "";

        paramsMap = Arrays.stream(queryString.split("&"))
                .map(part -> part.split("=", 2))
                .filter(bits -> bits.length == 2 && !bits[0].isBlank() && !bits[1].isBlank())
                .collect(Collectors.toMap(
                        bits -> bits[0].trim(),
                        bits -> bits[1].trim()
                ));
    }

    public String getParam(String paramName, String defaultValue) {
        return paramsMap.getOrDefault(paramName, defaultValue);
    }

    public int getParamAsInt(String paramName, int defaultValue) {
        String value = getParam(paramName, "");

        if (value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
