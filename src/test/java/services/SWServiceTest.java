package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.holocron.HolCharacter;
import model.swapi.SWCharacter;
import model.swapi.SWShip;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SWServiceTest{

    private Utils utils = new Utils();
    private final Properties properties = utils.getProperties();
    private String uri = properties.getProperty("swapi.uri");
    private String shipsEndpoint = properties.getProperty("swapi.starships.endpoint");
    private String peopleEndpoint = properties.getProperty("swapi.people.endpoint");
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SWServiceTest.class);
    private static final String UNKNOWN_SPECIES = "Unknown species";

    @Test
    public void testAPIConnection(){


        String response = "";
        List<SWShip> starships;

        try{
            response = utils.makeAPICall(uri,shipsEndpoint);
        }
        catch(Exception e){
            logger.error("Error trying to test ships retrieval. Error: " + e);
        }

        JsonObject element = new Gson().fromJson(response,JsonObject.class);
        JsonElement results = element.get("results");
        Gson gson = new Gson();
        starships = Arrays.asList(gson.fromJson(results,SWShip[].class));

        assertFalse(starships.isEmpty());
        assertThat(starships,Matchers.<SWShip>hasSize(10));
    }

    @Test
    public void testPaginationRetrieval(){

        String response = "";
        String previous = null;
        List<SWShip> starships = new ArrayList<>();
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){
                        response = utils.makeAPICall(uri,shipsEndpoint);
                    }
                }
                else{
                    response = utils.makeAPICall(next,"");
                }
            }
            catch(Exception e){
                logger.error("Error trying to test ships retrieval. Error: " + e);
            }
            JsonObject element = new Gson().fromJson(response,JsonObject.class);
            JsonElement nextElement = element.get("next");
            JsonElement previousElement = element.get("previous");
            JsonElement starshipsElement = element.get("results");
            Gson gson = new Gson();
            previous = gson.fromJson(previousElement,String.class);
            next = gson.fromJson(nextElement,String.class);
            starships.addAll(Arrays.asList(gson.fromJson(starshipsElement,SWShip[].class)));
        }
        while(next != null);

        assertEquals(36, starships.size());
    }

    @Test
    public void testCharacterSpecies(){
        String response = "";
        String previous = null;
        List<SWCharacter> people = new ArrayList<>();
        List<HolCharacter> characters;
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){
                        response = utils.makeAPICall(uri,peopleEndpoint);
                    }
                }
                else{
                    response = utils.makeAPICall(next,"");
                }
            }
            catch(Exception e){
                logger.error("Error trying to retrieve characters. Error: " + e);
            }
            JsonObject element = new Gson().fromJson(response,JsonObject.class);
            JsonElement nextElement = element.get("next");
            JsonElement previousElement = element.get("previous");
            JsonElement charactersElement = element.get("results");
            Gson gson = new Gson();
            previous = gson.fromJson(previousElement,String.class);
            next = gson.fromJson(nextElement,String.class);
            people.addAll(Arrays.asList(gson.fromJson(charactersElement,SWCharacter[].class)));
        }
        while(next != null);

        characters = peopleToCharacters(people);
        assertEquals("Droid", characters.get(1).getSpecie());
    }

    public List<HolCharacter> peopleToCharacters(List<SWCharacter> people){

        List<HolCharacter> characters = new ArrayList<>();

        for(SWCharacter person : people){

            HolCharacter character = new HolCharacter();
            character.setName(person.getName());

            if(!person.getSpecies().isEmpty()){
                character.setSpecie(getCharacterSpecie(person.getSpecies().get(0)));
            }else{
                character.setSpecie(UNKNOWN_SPECIES);

            }

            characters.add(character);
        }

        return characters;
    }

    public String getCharacterSpecie(String url){

        String species = "";
        try{
            String response = utils.makeAPICall(url, "");
            JsonObject element = new Gson().fromJson(response,JsonObject.class);
            JsonElement speciesName = element.get("name");
            Gson gson = new Gson();
            species = gson.fromJson(speciesName,String.class);
        }
        catch(Exception e){
            logger.error("Error trying to retrieve characters species. Error: " + e);
        }

        return species;
    }
}
