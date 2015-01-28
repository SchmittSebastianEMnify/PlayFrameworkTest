package controllers;

import static play.data.Form.form;
import static play.libs.Json.toJson;

import models.Person;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

import javax.persistence.PersistenceException;

public class Application extends Controller {

  public static Result index() {
    if (flash("fail") != null) {
      return ok(index.render(flash("fail"), form(Person.class)));
    } else if (flash("success") != null) {
      return ok(index.render(flash("success"), form(Person.class)));
    } else {
      return ok(index.render("No Error =)", form(Person.class)));
    }
  }

  public static Result addPerson() {
    try {
      Person person = form(Person.class).bindFromRequest("name").get();
      person.save();
      flash("success", "Name " + person.name + " with " + person.id + " added!");
    } catch (Exception e) {
      if (e instanceof IllegalStateException) {
        flash("fail", "Name empty -> None added!");
      } else if (e instanceof PersistenceException) {
        flash("fail", "Name too long!");
      } else {
        flash("fail", "Unknown Error! " + e.getMessage());
      }
    }
    return redirect(routes.Application.index());
  }

  public static Result getPersons() {
    List<Person> persons = new Model.Finder(String.class, Person.class).all();
    return ok(toJson(persons));
  }

  public static Result deletePerson() {
    try {
      Person person = Form.form(Person.class).bindFromRequest().get();
      List<Person> persons = new Model.Finder(String.class, Person.class).all();
      Integer deleted = 0;
      for (Person p : persons) {
        if (p.name.equals(person.name)) {
          p.delete();
          deleted++;
        }
      }

      flash("success", "Deleted " + deleted + " Names!");
    } catch (Exception e) {
      flash("fail", "Name empty -> None deleted!");
    }
    return redirect(routes.Application.index());
  }
}
