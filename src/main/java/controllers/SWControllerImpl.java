package controllers;

import com.google.gson.Gson;
import model.SWStarship;
import services.SWService;

import java.util.List;

public class SWControllerImpl implements SWController{

    private final SWService service;

    public SWControllerImpl(SWService service){
        this.service = service;
    }

    @Override
    public String getResult(String entity){

        String result = "";

        if(entity.equals("ships")){
            result = service.getStarships();

        }else if(entity.equals("characters")){
            result = service.getCharacters();
        }

        return result;
    }
}
