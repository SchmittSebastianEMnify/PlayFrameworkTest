$ ->
  $.get "/persons", (persons) ->
    $.each persons, (index, person) -> 
      $("#persons").append $("<li>").text( person.id + " " + person.name)