package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.SWStarship;
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
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SWServiceTest.class);


    //If these tests ever fails it's either because the endpoint is dead or the per page and/or the total amount of ships have changed
    @Test
    public void testAPIConnection(){


        String response = "";
        List<SWStarship> starships;

        try{
            response = utils.makeAPICall(uri,shipsEndpoint);
        }
        catch(Exception e){
            logger.error("Error trying to test ships retrieval. Error: " + e);
        }

        JsonObject element = new Gson().fromJson(response,JsonObject.class);
        JsonElement results = element.get("results");
        Gson gson = new Gson();
        starships = Arrays.asList(gson.fromJson(results,SWStarship[].class));

        assertFalse(starships.isEmpty());
        assertThat(starships,Matchers.<SWStarship>hasSize(10));

    }

    @Test
    public void testPaginationRetrieval(){

        String response = "";
        String previous = null;
        List<SWStarship> starships = new ArrayList<>();
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
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
            starships.addAll(Arrays.asList(gson.fromJson(starshipsElement,SWStarship[].class)));
        }
        while(next != null);

        assertEquals(36, starships.size());


    }
}
