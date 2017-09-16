package main.java.interpreter.commands;

import main.java.interpreter.PlayerScriptCommand;
import main.java.utilties.entities.Player;
import main.java.utilties.models.Location;
import main.java.utilties.models.Map;

public class MoveCommand implements PlayerScriptCommand{

    /**
     * Move the Player to location on Map.  If location is occupied, player will not move.
     * @param map Map
     * @param location Location to move to
     * @param player Player to move
     */
    @Override
    public void performAction(Map map, Location location, Player player) {
        if(!map.isLocationOccupied(location)){
            map.setLocation(player, location);
        }
    }
}
