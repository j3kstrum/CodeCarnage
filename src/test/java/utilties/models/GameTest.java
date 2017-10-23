package utilties.models;

import common.exceptions.LoadMapFailedException;
import org.junit.Before;
import org.junit.Test;
import org.mapeditor.io.MapReader;
import utilties.entities.Player;
import utilties.models.EntityMap;
import utilties.models.Game;

import java.awt.*;
import java.net.URL;

import static org.junit.Assert.assertTrue;

public class GameTest {

    private Game game;
    private Player player;

    @Before
    public void setup() throws Exception {
        try {
            URL mapPath = new URL("https://www.cse.buffalo.edu/~jacobeks/codecarnage/me/r/game-map.tmx");
            MapReader mr = new MapReader();
            EntityMap entityMap = new EntityMap(mr.readMap(mapPath), 25, 15);
            game = new Game(entityMap);
            player = game.getPlayer(Game.PLAYER_ID);
        } catch (Exception ex) {
            throw new LoadMapFailedException(ex.getMessage());
        }

    }

    @Test
    public void containsPlayer() throws Exception {
        assertTrue(game.getPlayer(Game.PLAYER_ID)!=null);

    }

    @Test
    public void containsOpponent() throws Exception {
        assertTrue(game.getPlayer(Game.OPPONENT_ID)!=null);
    }


    @Test
    public void isNotShieldingOnStartup() throws Exception {
        assertTrue(player.isShielding() == false);
    }

    @Test
    public void updateLocationOfPlayer() throws Exception {
        Point moveRight = new Point(player.getLocation().x + 1, player.getLocation().y);
        game.move(Game.PLAYER_ID, 1, 0);
        assertTrue(player.getLocation().x == moveRight.x &&  player.getLocation().y == moveRight.y);
    }

    @Test
    public void cannotMoveOutOfBounds() throws Exception {
        Point moveRight = new Point(player.getLocation().x + 25, player.getLocation().y);
        game.move(Game.PLAYER_ID, 25, 0);
        assertTrue(player.getLocation().x != moveRight.x &&  player.getLocation().y == moveRight.y);
    }

    @Test
    public void removeShieldingHeal() throws Exception {
        game.defend(Game.PLAYER_ID);
        game.heal(Game.PLAYER_ID, 5);
        assertTrue(player.isShielding() == false);
    }

    @Test
    public void removeShieldingApproach() throws Exception {
        game.defend(Game.PLAYER_ID);
        game.approach(Game.PLAYER_ID, Game.OPPONENT_ID);
        assertTrue(player.isShielding() == false);
    }

    @Test
    public void removeShieldingDoNothing() throws Exception {
        game.defend(Game.PLAYER_ID);
        game.doNothing(Game.PLAYER_ID);
        assertTrue(player.isShielding() == false);
    }

    @Test
    public void heal() throws Exception {
        player.setHealth(50);
        game.heal(Game.PLAYER_ID, 10);
        assertTrue(player.getHealth() == 60);
    }

    @Test
    public void healthOver100() throws Exception {
        game.heal(Game.PLAYER_ID, 10);
        assertTrue(player.getHealth() == 100);
    }

    @Test
    public void approachOpponent() throws Exception {
        int oldDistance = game.pathDistanceToPlayer(Game.PLAYER_ID, Game.OPPONENT_ID);
        game.approach(Game.PLAYER_ID, Game.OPPONENT_ID);
        int newDistance = game.pathDistanceToPlayer(Game.PLAYER_ID, Game.OPPONENT_ID);

        assertTrue(oldDistance > newDistance);
    }

    @Test
    public void attackOpponent() throws Exception {
        Point opponentLocation = game.getPlayer(Game.OPPONENT_ID).getLocation();
        game.move(Game.PLAYER_ID, opponentLocation.x - player.getLocation().x - 1, opponentLocation.y - player.getLocation().y);
        game.attack(Game.PLAYER_ID);
        assertTrue(game.getPlayer(Game.OPPONENT_ID).getHealth() == 100 - player.getDamage());
    }

    @Test
    public void attackOpponentWhileDefending() throws Exception {
        Point opponentLocation = game.getPlayer(Game.OPPONENT_ID).getLocation();
        game.move(Game.PLAYER_ID, opponentLocation.x - player.getLocation().x - 1, opponentLocation.y - player.getLocation().y);
        game.defend(Game.OPPONENT_ID);
        game.attack(Game.PLAYER_ID);
        assertTrue(game.getPlayer(Game.OPPONENT_ID).getHealth() == (100 - player.getDamage() + player.getShieldStrength()));
    }

    @Test
    public void attackOpponentTillDeath() throws Exception {
        Point opponentLocation = game.getPlayer(Game.OPPONENT_ID).getLocation();
        game.move(Game.PLAYER_ID, opponentLocation.x - player.getLocation().x - 1, opponentLocation.y - player.getLocation().y);
        game.attack(Game.PLAYER_ID); //90
        game.attack(Game.PLAYER_ID); //80
        game.attack(Game.PLAYER_ID); //70
        game.attack(Game.PLAYER_ID); //60
        game.attack(Game.PLAYER_ID); //50
        game.attack(Game.PLAYER_ID); //40
        game.attack(Game.PLAYER_ID); //30
        game.attack(Game.PLAYER_ID); //20
        game.attack(Game.PLAYER_ID); //10
        game.attack(Game.PLAYER_ID); //0

        assertTrue(game.isDead(Game.OPPONENT_ID));
    }


    @Test
    public void isGameOver() throws Exception {
        Point opponentLocation = game.getPlayer(Game.OPPONENT_ID).getLocation();
        game.move(Game.PLAYER_ID, opponentLocation.x - player.getLocation().x - 1, opponentLocation.y - player.getLocation().y);
        game.attack(Game.PLAYER_ID); //90
        game.attack(Game.PLAYER_ID); //80
        game.attack(Game.PLAYER_ID); //70
        game.attack(Game.PLAYER_ID); //60
        game.attack(Game.PLAYER_ID); //50
        game.attack(Game.PLAYER_ID); //40
        game.attack(Game.PLAYER_ID); //30
        game.attack(Game.PLAYER_ID); //20
        game.attack(Game.PLAYER_ID); //10
        game.attack(Game.PLAYER_ID); //0
        assertTrue(game.isGameOver());
    }

    @Test
    public void isGameOverStalemate() throws Exception {
        for(int i = 0; i < Game.NUMBER_OF_TURNS_TO_STALEMATE; i++){
            game.nextTurn();
        }
        assertTrue(game.isGameOver());
    }

    @Test
    public void isStalemateFlagSet() throws Exception {
        for(int i = 0; i < Game.NUMBER_OF_TURNS_TO_STALEMATE; i++){
            game.nextTurn();
        }
        assertTrue(game.isStalemate());
    }

    @Test
    public void isStalemateOnePlayerDoesNotMove() throws Exception {
        boolean evade = true;
        for(int i = 0; i < Game.NUMBER_OF_TURNS_TO_STALEMATE; i++){
            if(evade){
                game.evade(Game.PLAYER_ID, Game.OPPONENT_ID);
            }
            else{
                if(!game.approach(Game.PLAYER_ID, Game.OPPONENT_ID)){
                    game.evade(Game.PLAYER_ID, Game.OPPONENT_ID);
                }
            }
            evade = !evade;
            game.nextTurn();
        }
        assertTrue(game.isStalemate());
    }
}
