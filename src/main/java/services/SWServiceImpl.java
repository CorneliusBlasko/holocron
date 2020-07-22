package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.SWCharacter;
import model.SWStarship;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SWServiceImpl implements SWService{

    private Utils utils = new Utils();
    Properties properties = utils.getProperties();
    String uri = properties.getProperty("swapi.uri");
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SWServiceImpl.class);


    @Override
    public String getStarships(){

        String response = "";
        String previous = null;
        List<SWStarship> starships = new ArrayList<>();
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
                        response = utils.makeAPICall(uri,"starships");
                    }
                }
                else{
                    response = utils.makeAPICall(next,"");
                }
            }
            catch(Exception e){
                logger.error("Error trying to retrieve ships. Error: " + e);
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

        return shipsToString(starships);
    }

    @Override
    public String getCharacters(){
        String response = "";
        String previous = null;
        List<SWCharacter> characters = new ArrayList<>();
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
                        response = utils.makeAPICall(uri,"people");
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
            characters.addAll(Arrays.asList(gson.fromJson(charactersElement,SWCharacter[].class)));
        }
        while(next != null);

        return charactersToString(characters);
    }

    public String shipsToString(List<SWStarship> ships){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"ships\":[");

        for(SWStarship ship : ships){
            Gson gson = new Gson();
            builder.append(gson.toJson(ship));
            if(ships.indexOf(ship) != ships.size() - 1){
                builder.append(',');
            }
            else{
                builder.append("]}");
            }
        }
        return builder.toString();
    }

    public String charactersToString(List<SWCharacter> characters){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"ships\":[");

        for(SWCharacter character : characters){
            Gson gson = new Gson();
            builder.append(gson.toJson(character));
            if(characters.indexOf(character) != characters.size() - 1){
                builder.append(',');
            }
            else{
                builder.append("]}");
            }
        }
        return builder.toString();
    }
}
