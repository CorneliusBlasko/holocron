package controllers;

import services.SWService;

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
