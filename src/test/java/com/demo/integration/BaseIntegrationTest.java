package com.demo.integration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.AuthorizerModule;
import org.junit.jupiter.api.BeforeEach;

public class BaseIntegrationTest {

    private Injector injector = Guice.createInjector(new AuthorizerModule());

    @BeforeEach
    public void setup() {
        injector.injectMembers(this);
    }
}
