package model;

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
public class SWStarship{

    private String name;
    private String model;
    private String starship_class;
    private String manufacturer;
    private String cost_in_credits;
    private String length;
    private String crew;
    private String passengers;
    private String max_atmosphere_speed;
    private String hyperdrive_rating;
    private String MGLT;
    private String cargo_capacity;
    private String consumables;
    private List<String> films;
    private List<String> pilots;
    private String url;
    private Date created;
    private Date edited;
}
