# Code Challenge: Authorizer

Authorizer is an application which authorizes transactions for a specific account following a set
of predefined rules, this application receives operations in JSON through stdin and sends response to stdout in JSON

###Technical and architectural decisions 
Application was built on 3 layers:
  * Controller: this layer receives all operations from stdin in JSON, call the next layer (Service) and returns all responses in JSON 
  * Service: in this layer we have all business rules like transaction validation, this layer is called from controller and it uses repository layer
  * Repository: this layer is responsible to store and read objects

Application uses these entities to store and process all operations:
 * OperationRequest: we have 2 different operations create account and transactions
 * OperationResponse and AccountResponse: contains the result of processing an operations (transaction or create account)
 * Account: all account information like available-limit and active card this is stored in memory
 * Transaction: it has all transaction information like merchant, amount and time
 * ViolationType: it is an enum with all possible violations
 
Storage: Repository layer  is composed by a GenericRepository interface and an abstract class that defines and implements common operations like save and find entities
Entities are stored in memory with a LinkedHashMap with this data structure we preserve the insertion order and we can retrieve and entity using a key, entities have an unique ID (UUID) that is used as key in the data structure
Entities that will be stored need to extend from BaseEntity

Account and Transaction Operations: operations are processed on Service layer (OperationService) when we process an operation, we must validate the operation, this validations was implemented using a chain of Responsibility design pattern

FrameWorks: Application was built on java 8 and using these frameworks:
 * Guice: is a lightweight dependency injection framework, I decide to use this framework instead of Spring because this is a small application 
 * Junit: framework for unit testing 
 * Mockito: framework to mock objects
 * Jackson: I use this framework to process JSON requests and build JSON response
 
### Testing
Every layer (Controller, Service and repository) have unit tests, the integration tests are in com.demo.integration.OperationControllerIT

## How to set up project
this project uses Apache Maven 3.5.4 as dependency manager and build automation tool
Steps to build application:

 * Go to the root folder of this project whe pom.xml file is located and run:
 
       mvn clean install 
 
Steps to run application:

 * main method to run this application is located in the class com.demo.Application
 * to run application we can use maven:
 
       mvn compile exec:java < input    
