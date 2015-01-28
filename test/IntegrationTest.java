import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;
import play.libs.F.Callback;
import play.mvc.Result;
import play.test.FakeRequest;
import play.test.TestBrowser;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTest {
  /**
   * add your integration test here in this example we just check if the welcome page is being shown
   */
  @Test
  public void test() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            browser.goTo("http://localhost:9000");
            assertThat(browser.pageSource()).contains("Add Person");
          }
        });
  }


  @Test
  public void testBadRoute() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            browser.goTo("http://localhost:9000");
            Result result = route(new FakeRequest("GET", "/person"));
            assertThat(result).isNull();
          }
        });
  }

  @Test
  public void testAddPerson() {
    running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT,
        new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
            FakeRequest fakeRequest = new FakeRequest("POST", "/person");
            Map<String, String> personMap = new HashMap<String, String>();
            personMap.put("name", "Egon");
            fakeRequest.withFormUrlEncodedBody(personMap);
            Result result = callAction(controllers.routes.ref.Application.addPerson(), fakeRequest);
            assertThat(browser.pageSource()).contains("Name Egon with");
          }
        });
  }
}
