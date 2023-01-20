import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class models the functions of the OhNoDragon game
 * 
 * @author Fatimah Mohammed
 */
public class OhNoDragon extends PApplet {
  private ArrayList<Room> roomList; // list of Rooms
  private File roomInfo; // room information
  private File mapInfo; // map of rooms
  private ArrayList<Character> characters; // array of characters
  private boolean isDragonTurn; // is its the dragon's turn
  private int gameState; // stop and go indication for game

  /**
   * Main method for the tester class
   * 
   * @param args list of input arguments if any
   */
  public static void main(String[] args) {
    PApplet.main("OhNoDragon"); // calls game to run
  }

  /**
   * Changes default settings of games visual window dimensions
   * 
   */
  @Override
  public void settings() {
    size(800, 600); // sets new dimensions
  }

  /**
   * Sets up images and variables to display in the game window
   * 
   */
  @Override
  public void setup() {
    this.getSurface().setTitle("OhNoDragon"); // sets the title of the window
    this.imageMode(PApplet.CORNER); // Images are drawn using the x,y-coordinate
    // as the top-left corner
    this.rectMode(PApplet.CORNERS); // When drawing rectangles interprets args
    // as top-left corner and bottom-right corner respectively
    this.focused = true; // window will be active upon running program
    this.textAlign(CENTER); // sets the text alignment to center
    this.textSize(20); // sets the font size for the text
    roomList = new ArrayList<Room>(); // initialize roomList array
    characters = new ArrayList<Character>(); // initialize characters array
    isDragonTurn = false; // initialize boolean
    gameState = 0; // initialize gameState

    // loads all images and creates all rooms
    PImage treasure = loadImage("images/treasure.jpg");
    TreasureRoom.setTreasureBackground(treasure);
    TreasureRoom treasureRoom = new TreasureRoom(6);
    PImage portal = loadImage("images/portal.png");
    PortalRoom.setPortalImage(portal);
    PortalRoom portalRoom = new PortalRoom(7, "portal", portal);

    // load attributes for game
    Room.setProcessing(this);
    roomInfo = new File("roominfo.txt");
    mapInfo = new File("map.txt");
    loadRoomInfo();
    loadMap();
    loadCharacters();
  }

  /**
   * This method draws the game components and sets the logic
   * 
   */
  @Override
  public void draw() {
    // Don't run game if player won or lost
    // player won
    if (gameState == 1) {
      return;
    } else if (gameState == 2) { // player lost
      return;
    }

    // draw players current Room and check for warnings
    Player player = null;
    Dragon dragon = null;
    for (int i = 0; i < characters.size(); i++) {
      for (int b = 0; b < characters.size(); b++) {
        // casts from character to dragon
        if (characters.get(i) instanceof Player) {
          Room playerRoom = characters.get(i).getCurrentRoom();
          playerRoom.draw();
          player = (Player) characters.get(i);

          // checks if player is in portal
          if (player.getCurrentRoom() instanceof PortalRoom) {
            player.teleport();
            System.out.println(PortalRoom.getTeleportMessage());
          }

          // checks if player can get key
          if (characters.get(b).getLabel().equals("KEYHOLDER")) {
            if (player.getCurrentRoom() == characters.get(b).getCurrentRoom()) {
              player.obtainKey();
              System.out.println("KEY OBTAINED");
            }
          }

          // checks if player is in treasueRoom and ends game if is
          if (player.getCurrentRoom() instanceof TreasureRoom && player.hasKey() == true) {
            gameState = 1;
            System.out.println("You win :)");
            return;
          }

          // in treasure room but doesn't have key
          if (player.getCurrentRoom() instanceof TreasureRoom && player.hasKey() == false) {
            System.out.println("You don't have the key. Keep looking.");
          }

          // checks for nearby treasure
          if (player.isTreasureNearby()) {
            System.out.println(TreasureRoom.getTreasureWarning());
          }

          // check for dragon turn
          if (dragon instanceof Dragon) {
            if (isDragonTurn) {
              dragon.changeRoom(dragon.pickRoom());
            }
          }
        }
      }
    }

    // casts from character to dragon
    for (int i = 0; i < characters.size(); i++) {
      if (characters.get(i) instanceof Dragon) {
        dragon = (Dragon) characters.get(i);

        // checks for when you encounter dragon
        if (player.getCurrentRoom().equals(dragon.getCurrentRoom())) {
          System.out.println(Dragon.getDragonEncounter());
          gameState = 2;
          System.out.println("You lost :(");
          player.getCurrentRoom().draw();
          return;
        }

        // checks for nearby dragon
        if (player.isDragonNearby(dragon)) {
          System.out.println(Dragon.getDragonWarning());
        }

        // alternate turns with dragon and player
        if (isDragonTurn == true && gameState == 0) {
          dragon.changeRoom(dragon.pickRoom());
          isDragonTurn = false;
        }
      }
    }
  }

  /**
   * lets user move the player
   */
  @Override 
  public void keyPressed() {
    char k = this.key;
    String keyPressed = String.valueOf(k);
    int goTo = 0;
    Player player = null;

    // find player
    for (int i = 0; i < characters.size(); i++) {
      if (characters.get(i) instanceof Player) {
        player = (Player) characters.get(i);
      }
    }

    // convert key from char to a int
    try {
      goTo = Integer.parseInt(keyPressed);
      if (!player.getAdjacentRooms().contains(getRoomByID(goTo)) && (gameState == 0)) {
        System.out.println("NOT A VALID ROOM!");
        return;
      }

      // change room based on key pressed
      if (goTo > 0 || goTo < 10) {
        player.changeRoom(getRoomByID(goTo));
      }

      // if room change is not valid
      if (player.changeRoom(getRoomByID(goTo)) == true) {
        isDragonTurn = true;
      }
    } catch (NumberFormatException e) {
      System.out.println("Not a number! Enter a number 1-9");
    }
  }

  /**
   * Loads in room info using the file stored in roomInfo
   * 
   */
  private void loadRoomInfo() {
    System.out.println("Loading rooms...");
    Scanner fileReader = null;
    try {

      // scanner to read from file
      fileReader = new Scanner(roomInfo);

      // read line by line until none left
      while (fileReader.hasNext()) {
        String nextLine = fileReader.nextLine();

        // parse info and create new room
        String[] parts = nextLine.split(" \\| ");
        int ID = Integer.parseInt(parts[1].trim()); // get the room id
        String imageName = null;
        String description = null;
        PImage image = null;
        Room newRoom = null;

        if (parts.length >= 3) {
          imageName = parts[2].trim();
          image = this.loadImage("images" + File.separator + imageName);

        }

        if (parts.length == 4) {
          description = parts[3].trim(); // get the room description
        }

        switch (parts[0].trim()) {
          case "S":
            newRoom = new StartRoom(ID, image);
            break;
          case "R":
            newRoom = new Room(ID, description, image);
            break;
          case "P":
            newRoom = new PortalRoom(ID, description, image);
            break;
          case "T":
            newRoom = new TreasureRoom(ID);
            break;
          default:
            break;
        }

        if (newRoom != null) {
          roomList.add(newRoom);
        }
      }
    } catch (IOException e) { // handle checked exception
      e.printStackTrace();
    } finally {
      if (fileReader != null)
        fileReader.close(); // close scanner regardless of what happened for security reasons :)
    }
  }

  /**
   * Loads in room connections using the file stored in mapInfo
   * 
   */
  private void loadMap() {
    System.out.println("Loading map...");
    Scanner fileReader = null;
    try {
      // scanner to read from file
      fileReader = new Scanner(mapInfo);

      // read line by line until none left
      while (fileReader.hasNext()) {

        // parse info
        String nextLine = fileReader.nextLine();
        String parts[] = nextLine.split(" ");
        int id = Integer.parseInt(parts[0]);

        Room toEdit = getRoomByID(id); // get the room we need to update info for adjacent rooms

        // add all the rooms to the adj room list of toEdit
        for (int i = 1; i < parts.length; i++) {
          Room toAdjAdd = getRoomByID(Integer.parseInt(parts[i]));
          toEdit.addToAdjacentRooms(toAdjAdd);
        }
      }
    } catch (IOException e) { // handle checked exception
      e.printStackTrace();
    } finally { // close scanner regardless of what happened for security reasons :)
      if (fileReader != null)
        fileReader.close();
    }
  }

  /**
   * Get the room objected associated with the given ID.
   * 
   * @param id the ID of the room to retrieve
   * @return the Room that corresponds to that id
   */
  private Room getRoomByID(int id) {
    int indexToEdit = roomList.indexOf(new Room(id, "dummy", null));
    Room toEdit = roomList.get(indexToEdit);
    return toEdit;
  }

  /**
   * Loads characters into game
   */
  private void loadCharacters() {
    System.out.println("Adding characters...");
    characters.add(new Character(getRoomByID(5), "KEYHOLDER"));
    characters.add(new Player(getRoomByID(1)));
    characters.add(new Dragon(getRoomByID(9)));
  }
}
