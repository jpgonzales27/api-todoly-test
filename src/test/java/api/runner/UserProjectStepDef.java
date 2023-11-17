package api.runner;
import api.config.Configuration;
import api.factoryRequest.FactoryRequest;
import api.factoryRequest.RequestInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import api.utils.RandomEmailGenerator;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserProjectStepDef {

    Response response;
    RequestInfo requestInfo= new RequestInfo();
    Map<String,String> variables = new HashMap<>();


    @Given("create a new user with body")
    public void createANewUserWithBody(String body) {

        String randomUSer = RandomEmailGenerator.generateRandomUser(4);
        String bodyModified = body.replaceAll("\\{randomUser\\}", randomUSer);
        Configuration.user = randomUSer;
        Configuration.password = randomUSer;
        requestInfo.setUrl(Configuration.host+"/api/user.json").setBody(bodyModified);
        response = FactoryRequest.make("post").send(requestInfo);
    }

    @Then("the status code should be {int}")
    public void theStatusCodeShouldBe(int expectedCode) {
        response.then()
                .statusCode(expectedCode);
    }

    @And("obtain the user token in todo.ly")
    public void obtainTheUserTokenInTodoLy() {

        String credentials = Base64.getEncoder()
                .encodeToString((Configuration.user+":"+Configuration.password).getBytes());

        requestInfo.setUrl(Configuration.host+"/api/authentication/token.json")
                .setHeader("Authorization","Basic "+credentials);

        response = FactoryRequest.make("get").send(requestInfo);
        // get token
        String token = response.then().extract().path("TokenString");
        requestInfo = new RequestInfo();
        requestInfo.setHeader("Token",token);
    }

    @When("send a {} request to {string} with body")
    public void sendPOSTRequestWithBody(String method,String url,String body) {
        requestInfo.setUrl(Configuration.host+this.replaceValues(url)).setBody(body);
        response = FactoryRequest.make(method).send(requestInfo);
    }

    @And("the attribute {string} should have the value {string}")
    public void theAttributeShouldHaveTheValue(String attribute, String expectedValue) {
        response.then().body(attribute,equalTo(expectedValue));
    }

    @And("save the value {string} in the variable {string}")
    public void saveTheValueInTheVariable(String attribute, String nameVariable) {
        variables.put(nameVariable,response.then().extract().path(attribute)+"");
    }

    private String replaceValues(String value){
        for (String key:variables.keySet() ) {
            value=value.replace(key,variables.get(key));
        }
        return value;
    }



}
