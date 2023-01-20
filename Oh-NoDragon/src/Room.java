import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class defines all room properties and their functions
 */
public class Room {
  private String description; // verbal description of the room
  private ArrayList<Room> adjRooms; // list of all rooms directly connect
  private final int ID; // a "unique" identifier for each room
  protected static PApplet processing; // PApplet object which the rooms will use to
                                       // draw stuff to the GUI
  private PImage image; // stores the image that corresponds to the background of a room

  /**
   * Constructor for a Room object. Initializes all instance data fields.
   * 
   * @param ID          - , the ID that this Room should have
   * @param description - , the verbal description this Room should have
   * @param image       - , the image that should be used as a background when drawing this Room.
   * 
   */
  public Room(int ID, String description, processing.core.PImage image) {
    this.description = description; // initialize description field
    this.adjRooms = new ArrayList<Room>(); // initialize adjRooms field
    this.ID = ID; // initialize ID field
    this.image = image; // initialize image field
  }

  /**
   * Getter for ID.
   * 
   * @return the ID of this Room
   */
  public int getID() {
    return this.ID;
  }

  /**
   * Getter for description.
   * 
   * @return the verbal description of this Room
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Getter for the list of adjacentRooms.
   * 
   * @return the list of adjacent rooms
   */
  public ArrayList<Room> getAdjacentRooms() {
    return this.adjRooms;
  }

  /**
   * Sets the processing for the class.
   * 
   * @param processing - , the PApplet that this room will use to draw to the window
   */
  public static void setProcessing(processing.core.PApplet processing) {
    Room.processing = processing;
  }

  /**
   * Adds the given room to the list of rooms adjacent to this room.
   * 
   * @param toAdd - , the room to be added
   */
  public void addToAdjacentRooms(Room toAdd) {
    this.adjRooms.add(toAdd);
  }

  /**
   * Checks whether or not the given room is adjacent to this room.
   * 
   * @param r - , the room to check for adjacency
   * @return true if it is adjacent, false otherwise
   */
  public boolean isAdjacent(Room r) {
    // checks if given room r is contained in the adjRooms array
    this.adjRooms = getAdjacentRooms();

    for (int i = 0; i < adjRooms.size(); i++) {
      if (adjRooms.get(i).equals(r)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Overrides Object.equals(). Determines if two objects are equal.
   * 
   * @param other - , the object to check against this Room
   * @return true if other is of type Room and has the same ID, false otherwise
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof Room) {
      Room otherRoom = (Room) other;
      return this.ID == otherRoom.ID;
    }
    return false;
  }

  /**
   * Overrides Object.toString(). Returns a string representation of a Room object.
   * 
   * @return Returns a string in the form of "<ID>: <description>\n Adjacent Rooms: <r1's ID> <r2's
   *         ID>" list of adjacent room IDs continues for all rooms adjacent to this Room.
   */
  @Override
  public String toString() {
    String s = this.ID + ": " + this.description + "\n Adjacent Rooms: ";
    for (int i = 0; i < adjRooms.size(); i++) {
      s += adjRooms.get(i).ID + " ";
    }
    return s;
  }

  /**
   * Draws this Room to the window by drawing the background image, a rectangle, and some text.
   */
  public void draw() {
    processing.image(image, 0, 0);
    processing.fill(-7028);
    processing.rect(0, 500, 800, 600);
    processing.fill(0);
    processing.text(toString(), 300, 525);
  }
}
