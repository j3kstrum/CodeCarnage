package utilties.scripting;

import utilties.entities.Player;
import utilties.models.Location;
import utilties.models.Map;

public class PlayerFunctions {

    public static void Move(Map map, Location location, Player player){
        if(!map.isOccupied(location)){
            map.setLocation(player, location);
        }
    }

}
