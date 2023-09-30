package com.demo;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.demo.repository.AccountRepository;
import com.demo.repository.TransactionRepository;
import com.demo.repository.impl.AccountRepositoryImpl;
import com.demo.repository.impl.TransactionRepositoryImpl;
import com.demo.service.AccountService;
import com.demo.service.ApplicationSessionService;
import com.demo.service.OperationService;
import com.demo.service.TransactionService;
import com.demo.service.impl.AccountServiceImpl;
import com.demo.service.impl.ApplicationSessionServiceImpl;
import com.demo.service.impl.OperationServiceImpl;
import com.demo.service.impl.TransactionServiceImpl;
import com.demo.validation.service.account.AccountAuthorizationService;
import com.demo.validation.service.account.DefaultAccountAuthorizationService;
import com.demo.validation.service.transaction.DefaultTransactionAuthorizationService;
import com.demo.validation.service.transaction.LimitedTransactionAuthorizationService;
import com.demo.validation.service.transaction.TransactionAuthorizationService;

public class AuthorizerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class).asEagerSingleton();
        bind(ApplicationSessionService.class).to(ApplicationSessionServiceImpl.class).asEagerSingleton();
        bind(OperationService.class).to(OperationServiceImpl.class).asEagerSingleton();
        bind(TransactionService.class).to(TransactionServiceImpl.class).asEagerSingleton();
        bind(AccountAuthorizationService.class).to(DefaultAccountAuthorizationService.class).asEagerSingleton();
        bind(AccountRepository.class).to(AccountRepositoryImpl.class).asEagerSingleton();
        bind(TransactionAuthorizationService.class).annotatedWith(Names.named("defaulTransactionAuthorizationService")).to(DefaultTransactionAuthorizationService.class).asEagerSingleton();
        bind(TransactionAuthorizationService.class).annotatedWith(Names.named("limitedTransactionAuthorizationService")).to(LimitedTransactionAuthorizationService.class).asEagerSingleton();
        bind(TransactionRepository.class).to(TransactionRepositoryImpl.class).asEagerSingleton();
    }
}
