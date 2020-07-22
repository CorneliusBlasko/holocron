package utils;

import java.util.Properties;

public class Utils{

    public Properties getProperties(){
        Properties properties = new Properties();
        try{
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        }
        catch(Exception e){

        }
        return properties;
    }

}
