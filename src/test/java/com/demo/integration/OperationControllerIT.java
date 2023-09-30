package com.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.demo.controller.OperationController;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationControllerIT extends BaseIntegrationTest {

    @Inject
    OperationController operationController;


    @Test
    void processTransactionSuccessfully() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        List<String> response = operationController.processOperations(requests);

        assertEquals(2, response.size());

        assertEquals(mapper.readTree("{\"account\": {\"active-card\": true, \"available-limit\": 100}, \"violations\": []}"), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree("{\"account\": {\"active-card\": true, \"available-limit\": 80}, \"violations\": []}"), mapper.readTree(response.get(1)));
    }

    @Test
    void processTransactionAccountNotInitialized() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"transaction\": {\"merchant\": \"Uber Eats\", \"amount\": 25, \"time\": \"2020-12-01T11:07:00.000Z\"}}");
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 225}}");
        requests.add("{\"transaction\": {\"merchant\": \"Uber Eats\", \"amount\": 25, \"time\": \"2020-12-01T11:07:00.000Z\"}}");

        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {}, \"violations\": [\"account-not-initialized\"]}";
        String expectedLine2 = "{\"account\": {\"active-card\": true, \"available-limit\": 225}, \"violations\": []}";
        String expectedLine3 = "{\"account\": {\"active-card\": true, \"available-limit\": 200}, \"violations\": []}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
    }

    @Test
    void processTransactionCardNotActive() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": false, \"available-limit\": 100}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Habbib's\", \"amount\": 15, \"time\": \"2019-02-13T11:15:00.000Z\"}}");

        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": []}";
        String expectedLine2 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": [\"card-not-active\"]}";
        String expectedLine3 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": [\"card-not-active\"]}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
    }

    @Test
    void processTransactionCardNotActiveLimited() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": false, \"available-limit\": 100}}");
        requests.add("{\"allow-list\": {\"active\": true}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Habbib's\", \"amount\": 15, \"time\": \"2019-02-13T11:15:00.000Z\"}}");

        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": []}";
        String expectedLine2 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": [\"card-not-active\"]}";
        String expectedLine3 = "{\"account\": {\"active-card\": false, \"available-limit\": 100}, \"violations\": [\"card-not-active\"]}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
    }

    @Test
    void processTransactionHighFrequency() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Habbib's\", \"amount\": 20, \"time\": \"2019-02-13T11:00:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"McDonald's\", \"amount\": 20, \"time\": \"2019-02-13T11:01:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Subway\", \"amount\": 20, \"time\": \"2019-02-13T11:01:31.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 10, \"time\": \"2019-02-13T12:00:00.000Z\"}}");


        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {\"active-card\": true, \"available-limit\": 100}, \"violations\": []}";
        String expectedLine2 = "{\"account\": {\"active-card\": true, \"available-limit\": 80}, \"violations\": []}";
        String expectedLine3 = "{\"account\": {\"active-card\": true, \"available-limit\": 60}, \"violations\": []}";
        String expectedLine4 = "{\"account\": {\"active-card\": true, \"available-limit\": 40}, \"violations\": []}";
        String expectedLine5 = "{\"account\": {\"active-card\": true, \"available-limit\": 40}, \"violations\": [\"high-frequency-small-interval\"]}";
        String expectedLine6 = "{\"account\": {\"active-card\": true, \"available-limit\": 30}, \"violations\": []}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
        assertEquals(mapper.readTree(expectedLine4), mapper.readTree(response.get(3)));
        assertEquals(mapper.readTree(expectedLine5), mapper.readTree(response.get(4)));
        assertEquals(mapper.readTree(expectedLine6), mapper.readTree(response.get(5)));
    }

    @Test
    void processTransactionHighFrequencyLimited() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        requests.add("{\"allow-list\": {\"active\": true}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Habbib's\", \"amount\": 20, \"time\": \"2019-02-13T11:00:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"McDonald's\", \"amount\": 20, \"time\": \"2019-02-13T11:01:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Subway\", \"amount\": 20, \"time\": \"2019-02-13T11:01:31.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 10, \"time\": \"2019-02-13T12:00:00.000Z\"}}");


        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {\"active-card\": true, \"available-limit\": 100}, \"violations\": []}";
        String expectedLine2 = "{\"account\": {\"active-card\": true, \"available-limit\": 80}, \"violations\": []}";
        String expectedLine3 = "{\"account\": {\"active-card\": true, \"available-limit\": 60}, \"violations\": []}";
        String expectedLine4 = "{\"account\": {\"active-card\": true, \"available-limit\": 40}, \"violations\": []}";
        String expectedLine5 = "{\"account\": {\"active-card\": true, \"available-limit\": 20}, \"violations\": []}";
        String expectedLine6 = "{\"account\": {\"active-card\": true, \"available-limit\": 10}, \"violations\": []}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
        assertEquals(mapper.readTree(expectedLine4), mapper.readTree(response.get(3)));
        assertEquals(mapper.readTree(expectedLine5), mapper.readTree(response.get(4)));
        assertEquals(mapper.readTree(expectedLine6), mapper.readTree(response.get(5)));
    }

    @Test
    void processTransactionDoubledTransaction() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:00.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"McDonald's\", \"amount\": 10, \"time\": \"2019-02-13T11:00:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:02.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 15, \"time\": \"2019-02-13T11:00:03.000Z\"}}");


        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\": {\"active-card\": true, \"available-limit\": 100}, \"violations\": []}";
        String expectedLine2 = "{\"account\": {\"active-card\": true, \"available-limit\": 80}, \"violations\": []}";
        String expectedLine3 = "{\"account\": {\"active-card\": true, \"available-limit\": 70}, \"violations\": []}";
        String expectedLine4 = "{\"account\": {\"active-card\": true, \"available-limit\": 70}, \"violations\":[\"doubled-transaction\"]}";
        String expectedLine5 = "{\"account\": {\"active-card\": true, \"available-limit\": 55}, \"violations\": []}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
        assertEquals(mapper.readTree(expectedLine4), mapper.readTree(response.get(3)));
        assertEquals(mapper.readTree(expectedLine5), mapper.readTree(response.get(4)));
    }

    @Test
    void processTransactionMultipleViolations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        requests.add("{\"transaction\": {\"merchant\": \"McDonald's\", \"amount\": 10, \"time\": \"2019-02-13T11:00:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:02.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 5, \"time\": \"2019-02-13T11:00:07.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 5, \"time\": \"2019-02-13T11:00:08.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 150, \"time\": \"2019-02-13T11:00:18.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 190, \"time\": \"2019-02-13T11:00:22.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 15, \"time\": \"2019-02-13T12:00:27.000Z\"}}");


        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}";
        String expectedLine2 = "{\"account\":{\"active-card\":true,\"available-limit\":90},\"violations\":[]}";
        String expectedLine3 = "{\"account\":{\"active-card\":true,\"available-limit\":70},\"violations\":[]}";
        String expectedLine4 = "{\"account\":{\"active-card\":true,\"available-limit\":65},\"violations\":[]}";
        String expectedLine5 = "{\"account\":{\"active-card\":true,\"available-limit\":65},\"violations\":[\"high-frequency-small-interval\",\"doubled-transaction\"]}";
        String expectedLine6 = "{\"account\":{\"active-card\":true,\"available-limit\":65},\"violations\":[\"insufficient-limit\",\"high-frequency-small-interval\"]}";
        String expectedLine7 = "{\"account\":{\"active-card\":true,\"available-limit\":65},\"violations\":[\"insufficient-limit\",\"high-frequency-small-interval\"]}";
        String expectedLine8 = "{\"account\":{\"active-card\":true,\"available-limit\":50},\"violations\":[]}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
        assertEquals(mapper.readTree(expectedLine4), mapper.readTree(response.get(3)));
        assertEquals(mapper.readTree(expectedLine5), mapper.readTree(response.get(4)));
        assertEquals(mapper.readTree(expectedLine6), mapper.readTree(response.get(5)));
        assertEquals(mapper.readTree(expectedLine7), mapper.readTree(response.get(6)));
        assertEquals(mapper.readTree(expectedLine8), mapper.readTree(response.get(7)));
    }
   @Test
    void processTransactionMultipleViolationsLimited() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<String> requests = new ArrayList<>();
        requests.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
       requests.add("{\"allow-list\": {\"active\": true}}");
       requests.add("{\"transaction\": {\"merchant\": \"McDonald's\", \"amount\": 10, \"time\": \"2019-02-13T11:00:01.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T11:00:02.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 5, \"time\": \"2019-02-13T11:00:07.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 5, \"time\": \"2019-02-13T11:00:08.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 150, \"time\": \"2019-02-13T11:00:18.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 190, \"time\": \"2019-02-13T11:00:22.000Z\"}}");
        requests.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 15, \"time\": \"2019-02-13T12:00:27.000Z\"}}");


        List<String> response = operationController.processOperations(requests);
        String expectedLine1 = "{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}";
        String expectedLine2 = "{\"account\":{\"active-card\":true,\"available-limit\":90},\"violations\":[]}";
        String expectedLine3 = "{\"account\":{\"active-card\":true,\"available-limit\":70},\"violations\":[]}";
        String expectedLine4 = "{\"account\":{\"active-card\":true,\"available-limit\":65},\"violations\":[]}";
        String expectedLine5 = "{\"account\":{\"active-card\":true,\"available-limit\":60},\"violations\":[]}";
        String expectedLine6 = "{\"account\":{\"active-card\":true,\"available-limit\":60},\"violations\":[\"insufficient-limit\"]}";
        String expectedLine7 = "{\"account\":{\"active-card\":true,\"available-limit\":60},\"violations\":[\"insufficient-limit\"]}";
        String expectedLine8 = "{\"account\":{\"active-card\":true,\"available-limit\":45},\"violations\":[]}";

        assertEquals(mapper.readTree(expectedLine1), mapper.readTree(response.get(0)));
        assertEquals(mapper.readTree(expectedLine2), mapper.readTree(response.get(1)));
        assertEquals(mapper.readTree(expectedLine3), mapper.readTree(response.get(2)));
        assertEquals(mapper.readTree(expectedLine4), mapper.readTree(response.get(3)));
        assertEquals(mapper.readTree(expectedLine5), mapper.readTree(response.get(4)));
        assertEquals(mapper.readTree(expectedLine6), mapper.readTree(response.get(5)));
        assertEquals(mapper.readTree(expectedLine7), mapper.readTree(response.get(6)));
        assertEquals(mapper.readTree(expectedLine8), mapper.readTree(response.get(7)));
    }

}
