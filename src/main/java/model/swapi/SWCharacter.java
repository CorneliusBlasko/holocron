package model.swapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SWCharacter{

    private String name;
    private String birth_year;
    private String eye_color;
    private String gender;
    private String hair_color;
    private String height;
    private String mass;
    private String skin_color;
    private String homeworld;
    private List<String> films;
    private List<String> species;
    private List<String> starships;
    private List<String> vehicles;
    private String url;
    private Date created;
    private Date edited;
}
