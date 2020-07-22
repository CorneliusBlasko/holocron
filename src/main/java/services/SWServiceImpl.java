package services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.holocron.HolCharacter;
import model.holocron.HolShip;
import model.swapi.SWCharacter;
import model.swapi.SWShip;
import org.slf4j.LoggerFactory;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SWServiceImpl implements SWService{

    private Utils utils = new Utils();
    private final Properties properties = utils.getProperties();
    private String uri = properties.getProperty("swapi.uri");
    private String shipsEndpoint = properties.getProperty("swapi.starships.endpoint");
    private String peopleEndpoint = properties.getProperty("swapi.people.endpoint");
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SWServiceImpl.class);
    private static final String UNKNOWN_SPECIES = "Unknown species";


    @Override
    public String getShips(){

        String response = "";
        String previous = null;
        List<SWShip> starships = new ArrayList<>();
        List<HolShip> ships;
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
                        response = utils.makeAPICall(uri,shipsEndpoint);
                    }
                }
                else{ //Succesive calls
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
            starships.addAll(Arrays.asList(gson.fromJson(starshipsElement,SWShip[].class)));
        }
        while(next != null);

        ships = starshipsToShips(starships);

        return shipsToJSON(ships);
    }

    @Override
    public String getCharacters(){
        String response = "";
        String previous = null;
        List<SWCharacter> people = new ArrayList<>();
        List<HolCharacter> characters;
        String next = null;

        do{
            try{
                if(null == next){
                    if(null == previous){ //First call
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

        return charactersToJSON(characters);
    }

    public List<HolShip> starshipsToShips(List<SWShip> starships){

        List<HolShip> ships = new ArrayList<>();

        for(SWShip starship : starships){

            HolShip ship = new HolShip(starship.getName(), starship.getManufacturer());
            ships.add(ship);
        }

        return ships;


    }

    public String shipsToJSON(List<HolShip> ships){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"ships\":[");

        for(HolShip ship : ships){
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

    public List<HolCharacter> peopleToCharacters(List<SWCharacter> people){

        List<HolCharacter> characters = new ArrayList<>();

        for(SWCharacter person : people){

            HolCharacter character = new HolCharacter();
            character.setName(person.getName());

            if(!person.getSpecies().isEmpty()){
                //Every character has a list of species but there's only one element
                //I'm not sure why "species" is a list given that a character can only belong to one species
                character.setSpecie(getCharacterSpecie(person.getSpecies().get(0)));
            }else{
                character.setSpecie(UNKNOWN_SPECIES);

            }

            characters.add(character);
        }

        return characters;

    }

    public String charactersToJSON(List<HolCharacter> characters){
        StringBuilder builder = new StringBuilder();
        builder.append("{\"characters\":[");

        for(HolCharacter character : characters){
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
