package com.demo.validation.service;

import java.util.List;

public class AuthorizationResult {

    private final boolean isValid;

    private List<String> violations;

    public AuthorizationResult(boolean isValid, List<String> violations) {
        this.isValid = isValid;
        this.violations = violations;
    }

    static AuthorizationResult valid() {
        return new AuthorizationResult(true, null);
    }

    public static AuthorizationResult invalid(List<String> violations) {
        return new AuthorizationResult(false, violations);
    }

    public boolean notValid() {
        return !isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getViolations() {
        return violations;
    }
}
