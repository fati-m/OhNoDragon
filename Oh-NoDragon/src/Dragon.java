import java.util.ArrayList;
import java.util.Random;

/**
 * This class defines character players functions
 */
public class Dragon extends Character implements Moveable {
  private Random randGen; // random num generator used for moving
  private static final String DRAGON_WARNING = "You hear a fire breathing nearby!\n";
  private static final String DRAGON_ENCOUNTER = "Oh no! You ran into the fire breathing dragon!\n";

  /**
   * Constructor for a Dragon object. Initializes all instance fields. The label should be "DRAGON"
   * by default.
   * 
   * @param currentRoom - , the room that the Dragon starts in
   * @throws IllegalArgumentException - with a descriptive message if currentRoom is not a
   *                                  TreasureRoom
   */
  public Dragon(Room currentRoom) {
    super(currentRoom, "DRAGON");
    if (!(currentRoom instanceof TreasureRoom)) {
      throw new IllegalArgumentException("Not a treasure room.");
    }
    randGen = new Random(); // initialize random field
  }

  /**
   * Moves the Dragon to the destination room.
   * 
   * @param destination - , the Room to change it to
   * @returns true if the change was successful, false otherwise
   */
  @Override
  public boolean changeRoom(Room destination) {
    if (canMoveTo(destination) == true) {
      setCurrentRoom(destination);
    }
    if (getCurrentRoom().equals(destination)) {
      return true;
    }
    return false;
  }

  /**
   * Checks if the dragon can move to the given destination. A valid move is the destination not a
   * PortalRoom.
   * 
   * @param destination - , the room to check if the dragon can move towards
   * @return true if they can, false otherwise
   */
  @Override
  public boolean canMoveTo(Room destination) {
    if (getAdjacentRooms().contains(destination)) {
      if (!(destination instanceof PortalRoom)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Picks randomly ONCE an adjacent room to move into.
   * 
   * @return the room that this Dragon should try to move into
   */
  public Room pickRoom() {
    // generate number from 0 to amount of adjacent rooms
    ArrayList<Room> adjRoomsToDragon = new ArrayList<Room>();
    adjRoomsToDragon = getCurrentRoom().getAdjacentRooms();
    int randRoom = randGen.nextInt(adjRoomsToDragon.size());

    // runs until random location brings player to a non-portal room
    while (canMoveTo(adjRoomsToDragon.get(randRoom)) == false) {
      if (adjRoomsToDragon.size() == 0) {
        System.out.print("No rooms adjacent to dragon.");
        break;
      }
      if (adjRoomsToDragon.size() == 1) {
        changeRoom(adjRoomsToDragon.get(randRoom));
        break;
      }
      randRoom = randGen.nextInt(adjRoomsToDragon.size());
    }
    changeRoom(adjRoomsToDragon.get(randRoom));
    return adjRoomsToDragon.get(randRoom);
  }

  /**
   * Getter for DRAGON_WARNING.
   * 
   * @return the string for warning about a dragon being nearby.
   */
  public static String getDragonWarning() {
    return DRAGON_WARNING;
  }

  /**
   * Getter for DRAGON_ENCOUNTER.
   * 
   * @return the string for letting the player know they ran into the dragon.
   */
  public static String getDragonEncounter() {
    return DRAGON_ENCOUNTER;
  }
}
