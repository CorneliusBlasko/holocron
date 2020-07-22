package controllers;

import services.SWService;

public class SWControllerImpl implements SWController{

    private final SWService service;

    public SWControllerImpl(SWService service){
        this.service = service;
    }

    @Override
    public String getResult(String entity){

        if(entity.equals("ships")){
            return service.getShips();

        }else if(entity.equals("characters")){
            return service.getCharacters();
        }

        return null;
    }
}
