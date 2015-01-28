import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.flash;
import static play.test.Helpers.status;

import org.junit.Test;
import play.mvc.Result;
import play.test.FakeRequest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are interested in mocking a
 * whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

  @Test
  public void simpleCheck() {
    int a = 1 + 1;
    assertThat(a).isEqualTo(2);
  }



  @Test
  public void testIndex() {
    Result result = callAction(controllers.routes.ref.Application.index());
    assertThat(status(result)).isEqualTo(OK);
    assertThat(contentType(result)).isEqualTo("text/html");
    assertThat(charset(result)).isEqualTo("utf-8");
    assertThat(contentAsString(result)).contains("Welcome to Play");
  }

  @Test
  public void testAddPersonFail() {
    FakeRequest fakeRequest = new FakeRequest("POST", "/person");
    Result result = callAction(controllers.routes.ref.Application.addPerson(), fakeRequest);
    Map<String, String> requestFlash = new HashMap<String, String>();
    requestFlash.put("fail", "Name empty -> None added!");
    assertThat(status(result)).isEqualTo(303);
    assertThat(flash(result)).isEqualTo(requestFlash);
  }

  @Test
  public void testAddPersonSuccess() {
    FakeRequest fakeRequest = new FakeRequest("POST", "/person");
    Map<String, String> personMap = new HashMap<String, String>();
    personMap.put("name", "Egon");
    fakeRequest.withFormUrlEncodedBody(personMap);
    System.out.println(fakeRequest.getWrappedRequest().toString());
    Result result = callAction(controllers.routes.ref.Application.addPerson(), fakeRequest);
    Map<String, String> requestFlash = new HashMap<String, String>();
    requestFlash.put("success", "Name " + personMap.get("name") + " with " + 1 + " added!");
    assertThat(status(result)).isEqualTo(303);
    assertThat(flash(result)).isEqualTo(requestFlash);
  }
}
