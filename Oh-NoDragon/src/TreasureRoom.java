import processing.core.PImage;

/**
 * This class defines the treasure rooms functions
 */
public class TreasureRoom extends Room {
  private static final String TREASURE_WARNING = "You sense that there is treasure nearby.\n";
  private static PImage treasureBackground; // the image ALWAYS used for treasure rooms

  /**
   * Constructor for a TresureRoom object and have a description of "In the back of this room, you
   * spot a treasure chest. It is locked...". Initializes all instance data fields.
   * 
   * @param ID - , the ID to give to this object
   */
  public TreasureRoom(int ID) {
    super(ID, "In the back of this room, you spot a treasure chest. It is locked...",
        treasureBackground);
  }

  /**
   * Getter for TREASURE_WARNING.
   * 
   * @returns the string for warning about treasure being nearby.
   */
  public static String getTreasureWarning() {
    return TREASURE_WARNING;
  }

  /**
   * Determines whether or not the player can open the treasure chest in the room.
   * 
   * @param p - , the Player to check if they can open the chest
   * @return true if the player has the key and is in this TreasureRoom, false otherwise
   */
  public boolean playerCanGrabTreasure(Player p) {
    if ((p.getCurrentRoom() instanceof TreasureRoom) && (p.hasKey() == true)) {
      return true;
    }
    return false;
  }

  /**
   * Sets the background image for the TreasureRoom class.
   * 
   * @param treasureBackground - , the image to be the background
   */
  public static void setTreasureBackground(processing.core.PImage treasureBackground) {
    TreasureRoom.treasureBackground = treasureBackground;
  }
}
