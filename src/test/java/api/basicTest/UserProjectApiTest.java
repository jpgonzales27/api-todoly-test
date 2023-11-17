package basicTest;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import api.utils.RandomEmailGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserProjectApiTest {
    @Test
    public void verifyCreateUserAndCRUDProject(){


        String randomUSer = RandomEmailGenerator.generateRandomUser(4);

        JSONObject userBody = new JSONObject();
        userBody.put("Email",randomUSer);
        userBody.put("FullName",randomUSer);
        userBody.put("Password",randomUSer);

        JSONObject body = new JSONObject();
        body.put("Content","UCB_project");
        body.put("Icon",8);


        JsonSchemaFactory schemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(SchemaVersion.DRAFTV4)
                        .freeze()).freeze();

        Response responseUser =given()
                .body(userBody.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/user.json");

        System.out.println(">>>>>>>>>>>> Response Post User <<<<<<<<<<<<<");

        responseUser.then()
                .statusCode(200)
                .body("Email",equalTo(randomUSer))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userSchema.json").using(schemaFactory))
                .log().all();

        Response  responseProject =given()
                .auth()
                .preemptive()
                .basic(randomUSer,randomUSer)
                .body(body.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/projects.json");

        System.out.println(">>>>>>>>>>>> Response Post Project <<<<<<<<<<<<<");

        responseProject.then()
                .statusCode(200)
                .body("Content",equalTo("UCB_project"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("projectSchema.json").using(schemaFactory))
                .log().all();

        // extraer una valor
        int idProject= responseProject.then().extract().path("Id");

        //read
        responseProject=given()
                .auth()
                .preemptive()
                .basic(randomUSer,randomUSer)
                .log().all()
                .when()
                .get("https://todo.ly/api/projects/"+idProject+".json");

        System.out.println(">>>>>>>>>>>> Response Get Project <<<<<<<<<<<<<");

        responseProject.then()
                .statusCode(200)
                .body("Content",equalTo("UCB_project"))
                .log().all();

        // update
        body.put("Content","UCB_Update");

        responseProject =given()
                .auth()
                .preemptive()
                .basic(randomUSer,randomUSer)
                .body(body.toString())
                .log().all()
                .when()
                .put("https://todo.ly/api/projects/"+idProject+".json");

        System.out.println(">>>>>>>>>>>> Response Put Project <<<<<<<<<<<<<");

        responseProject.then()
                .statusCode(200)
                .body("Content",equalTo("UCB_Update"))
                .log().all();


        //delete
        responseProject=given()
                .auth()
                .preemptive()
                .basic(randomUSer,randomUSer)
                .log().all()
                .when()
                .delete("https://todo.ly/api/projects/"+idProject+".json");

        System.out.println(">>>>>>>>>>>> Response Delete Project <<<<<<<<<<<<<");

        responseProject.then()
                .statusCode(200)
                .body("Content",equalTo("UCB_Update"))
                .body("Deleted",equalTo(true))
                .log().all();


    }
}
