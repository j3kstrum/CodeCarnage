package main.java.utilties.scripting;

import main.java.utilties.entities.Player;
import main.java.utilties.models.Location;
import main.java.utilties.models.Map;

public class PlayerFunctions {

    public static void Move(Map map, Location location, Player player){
        if(!map.isTileOccupied(location)){
            map.setLocation(player, location);
        }
    }

}
