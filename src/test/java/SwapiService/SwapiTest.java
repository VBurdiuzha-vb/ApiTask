package SwapiService;

import com.fasterxml.jackson.databind.JsonNode;
import dto.PeopleDto;
import dto.Result;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import services.SwapiService;

import java.util.*;
import java.util.stream.Collectors;

import static Utils.SchemaGenerator.generateSchemaFromDto;

public class SwapiTest extends SwapiService {


    @Test
    public void useCase() {
        // 1 Search for a person with the name Vader.
        List<Result> peopleResultsResponse = sendGetPeople().as(PeopleDto.class).getResults();
        Result vaderObj = peopleResultsResponse.stream()
                .filter(e -> e.getName().contains("Vader")).collect(Collectors.toList()).get(0);

        // 2 Using previous response (1) find which film that Darth Vader joined has the less planets.
        List<String> filmsList = new ArrayList<>(vaderObj.getFilms());
        Map<String, Integer> planetsInFilm = new HashMap<>();

        for (int i = 0; i < filmsList.size(); i++) {
            var films = sendGetFilms(filmsList.get(i));
            planetsInFilm.put(filmsList.get(i), films.getPlanets().size());
        }

        var minPlanetsFilm = Collections.min(planetsInFilm.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("film that Darth Vader joined has the less planets is : " + minPlanetsFilm);

        // 3 Using previous responses (1) & (2) verify if Vader's starship is on film from response (2).
        List<String> startShipsOfMinPlanetsFilm = sendGetFilms(minPlanetsFilm).getStarships();
        var vadersStarshipOnFilm = vaderObj.getStarships().stream().filter(e ->
                startShipsOfMinPlanetsFilm.contains(e)).collect(Collectors.toList());
        vadersStarshipOnFilm.forEach(e -> System.out.println("Vaders starships on film : " + e));

        // 4 Find the oldest person ever played in all Star Wars films with less than 10 requests.
        List<Result> allFilmsPlayed = peopleResultsResponse.stream()
                .filter(e -> e.getFilms().size() == 6).collect(Collectors.toList());

        Map<String, Integer> personsBirthYears = new HashMap<>();
        for (int i = 0; i < allFilmsPlayed.size(); i++) {
            Integer bd = Integer.valueOf(allFilmsPlayed.get(i)
                    .getBirth_year().replaceAll("[^0-9]", ""));
            personsBirthYears.put(allFilmsPlayed.get(i).getName(), bd);
        }

        String oldestPersonAllFilmPlayed =
                Collections.min(personsBirthYears.entrySet(), Map.Entry.comparingByValue()).getKey();
        System.out.println("Oldest person ever played in all Star Wars films is : " + oldestPersonAllFilmPlayed);
    }

    // 5 Create contract (Json schema validation) test for /people API.
    @Test
    public void validateContractForGetPeople() {
        Response peopleResponse = sendGetPeople();
        PeopleDto convertedToDtoPeopleResponse = peopleResponse.as(PeopleDto.class);
        JsonNode jsonSchema = generateSchemaFromDto(convertedToDtoPeopleResponse.getClass());
        peopleResponse.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(String.valueOf(jsonSchema)));
    }
}
