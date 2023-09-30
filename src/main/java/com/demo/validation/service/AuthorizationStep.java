package com.demo.validation.service;

import java.util.List;

//chain of responsability design pattern
public abstract class AuthorizationStep<T> {

    private AuthorizationStep<T> next;

    public AuthorizationStep<T> linkWith(AuthorizationStep<T> next) {
        this.next = next;
        return next;
    }

    public abstract AuthorizationResult verify(T toValidate, List<String> violations);

    protected final AuthorizationResult checkNext(T toValidate, List<String> violations) {
        if (next == null) {
            if (violations == null || violations.isEmpty()) {
                return AuthorizationResult.valid();
            } else {
                return AuthorizationResult.invalid(violations);
            }
        }

        return next.verify(toValidate, violations);
    }
}
