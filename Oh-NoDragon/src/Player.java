/**
 * This class defines character players functions
 */
public class Player extends Character implements Moveable {
  private boolean hasKey; // key for the treasure

  /**
   * Constructor for player object. The label should be "PLAYER" and not have a key by default.
   * 
   * @param currentRoom - , the room that the player should start in
   * @throws IllegalArgumentException - if the currentRoom is not a StartRoom
   */
  public Player(Room currentRoom) {
    super(currentRoom, "PLAYER");
    hasKey = false; // initialize boolean
    if (!(currentRoom instanceof StartRoom)) {
      throw new IllegalArgumentException("Not a start room.");
    }
  }

  /**
   * Determines if the player has the key.
   * 
   * @return true if the player has the key, false otherwise
   */
  public boolean hasKey() {
    return this.hasKey;
  }

  /**
   * Gives player the key.
   *
   */
  public void obtainKey() {
    this.hasKey = true;
  }

  /**
   * Moves the Player to the destination room.
   * 
   * @param destination - , the Room to change it to
   * @return true if the change was successful, false otherwise
   */
  @Override
  public boolean changeRoom(Room destination) {
    if (getCurrentRoom().isAdjacent(destination)) {
      setCurrentRoom(destination);
    }

    // checks if change was successful
    if (getCurrentRoom().equals(destination)) {
      return true;
    }
    return false;
  }

  /**
   * Checks if the player can move to the given destination. A valid move is the destination is a
   * room adjacent to the player.
   * 
   * @param destination - , the room to check if the player can move towards
   * @return true if they can, false otherwise
   */
  @Override
  public boolean canMoveTo(Room destination) {
    if (getAdjacentRooms().contains(destination)) {
      return true;
    }
    return false;
  }

  /**
   * Checks if the player needs to teleport and move them if needed.
   * 
   * @return true if a teleport occurred, false otherwise
   */
  public boolean teleport() {
    if (getCurrentRoom() instanceof PortalRoom) {
      // generate random number and return room corresponding to that number
      PortalRoom temp = (PortalRoom) getCurrentRoom();
      Room temp2 = temp.getTeleportLocation();
      setCurrentRoom(temp2);
      // checks if teleport occurred
      if (getCurrentRoom() != temp) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether or not the given dragon is nearby. A dragon is considered nearby if it is in
   * one of the adjacent rooms.
   * 
   * @param the dragon to check if nearby
   * @return true if the dragon is nearby, false otherwise
   */
  public boolean isDragonNearby(Dragon d) {
    for (int i = 0; i < getAdjacentRooms().size(); i++) {
      if (getAdjacentRooms().get(i).equals(d.getCurrentRoom())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether or not a portal room is nearby. A portal room is considered nearby if it is
   * one of the adjacent rooms.
   * 
   * @return true if a portal room is nearby, false otherwise
   */
  public boolean isPortalNearby() {
    for (int i = 0; i < getAdjacentRooms().size(); i++) {
      if (getAdjacentRooms().get(i) instanceof PortalRoom) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines whether or not the treasure room is nearby. The treasure room is considered nearby
   * if it is one of the adjacent rooms.
   * 
   * @return true if the treasure room is nearby, false otherwise
   */
  public boolean isTreasureNearby() {
    for (int i = 0; i < getAdjacentRooms().size(); i++) {
      if (getAdjacentRooms().get(i) instanceof TreasureRoom) {
        return true;
      }
    }
    return false;
  }
}
