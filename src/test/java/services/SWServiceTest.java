package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.SWStarship;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SWServiceTest{

    private String uri;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SWServiceTest.class);

    private Utils utils = new Utils();

    @BeforeAll
    public void setProperties(){

        Properties properties = utils.getProperties();
        uri = properties.getProperty("swapi.uri");
    }

    @Test
    public void testAPI(){

        String response = "";
        List<SWStarship> starships;

        try{
            response = makeAPICall(uri,"starships");
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
    public void testNext(){

        String response = "";
        String previous = null;
        List<SWStarship> starships = new ArrayList<>();
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
                        response = makeAPICall(uri,"starships");
                    }
                }
                else{
                    response = makeAPICall(next,"");
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

    private String makeAPICall(String uri,String parameter) throws URISyntaxException, IOException{
        String response_content;
        URIBuilder query;

        if(parameter.isEmpty()){
            query = new URIBuilder(uri);
        }
        else{
            query = new URIBuilder(uri + "/" + parameter);
        }

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT,"application/json");

        CloseableHttpResponse response = client.execute(request);

        try{
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }
        finally{
            response.close();
        }

        return response_content;
    }
}
