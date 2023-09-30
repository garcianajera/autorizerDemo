package com.demo;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.controller.OperationController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        List<String> input = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            input.add(scanner.nextLine());
        }
        new Application().startAuthorizer(input);

    }

    private void startAuthorizer(List<String> input) {

        Injector injector = Guice.createInjector(new AuthorizerModule());
        OperationController operationController = injector.getInstance(OperationController.class);
        List<String> response = operationController.processOperations(input);
        response.forEach(System.out::println);
    }

}
