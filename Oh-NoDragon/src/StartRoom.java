import processing.core.PImage;

/**
 * This class defines where the starting point of the game
 */
public class StartRoom extends Room {
  public StartRoom(int ID, PImage image) {
    super(ID, "You find yourself in the entrance to a cave holding treasure.", image);
  }
}
