package utilties.scripting;

import utilties.entities.Player;
import utilties.models.Location;
import utilties.models.Map;

/**
 * Basic implementation of functions for Scripting Language
 */
public class PlayerFunctions {

    //TODO Add More scripts based on scripting language design

    /**
     * Move the Player to location on Map.  If location is occupied, player will not move.
     * @param map Map
     * @param location Location to move to
     * @param player Player to move
     */
    public static void Move(Map map, Location location, Player player){
        if(!map.isLocationOccupied(location)){
            map.setLocation(player, location);
        }
    }

}
