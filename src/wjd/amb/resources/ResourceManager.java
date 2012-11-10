/* @author william */
package wjd.amb.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import wjd.math.Rect;

/**
 *
 * @author wjd
 * @since jan-2012
 */
public class ResourceManager
{
  /// CLASS NAMESPACE CONSTANTS
  // extensions

  public static final String IM_EXT = ".png";
  public static final String IM_DIR = "";
  // names
  public static final String PLAYER = "fox";
  /// CLASS NAMESPACE VARIABLES
  private static ResourceManager instance = null;

  /// CLASS NAMESPACE FUNCTIONS
  public static ResourceManager getInstance()
  {
    if (instance == null)
      instance = new ResourceManager();
    return instance;
  }
  /// ATTRIBUTES
  private HashMap<String, BufferedImage> images;
  private HashMap<String, Graphic> graphics;
  private HashMap<String, Animation> animations;

  /// METHODS
  // creation
  private ResourceManager()
  {
    images = new HashMap<String, BufferedImage>();
    graphics = new HashMap<String, Graphic>();
    animations = new HashMap<String, Animation>();

    addImage(PLAYER);
    addAnimation("player_idle", PLAYER, new Rect(0, 0, 56, 80), 3,
                 Animation.LoopType.ALTERNATE_DIRECTION);
    addGraphic("player_graphic", PLAYER, new Rect(0, 0, 56, 80));
  }

  // query
  public BufferedImage getImage(String image_name)
  {
    return images.get(image_name);
  }

  public Graphic getGraphic(String graphic_name)
  {
    return graphics.get(graphic_name);
  }

  public Animation getAnimation(String animation_name)
  {
    return animations.get(animation_name);
  }

  // addition
  protected void addImage(String name)
  {
    try
    {
      BufferedImage image = ImageIO.read(new File(IM_DIR + name + IM_EXT));
      images.put(name, image);
    }
    catch (IOException ex)
    {
      Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  protected void addGraphic(String graphic_name, String image_name,
                            Rect frame)
  {
    graphics.put(graphic_name, new Graphic(images.get(image_name), frame));
  }

  protected void addAnimation(String anim_name, String image_name,
                              Rect frame, int n_frames, Animation.LoopType loopType)
  {
    Animation new_anim = new Animation(images.get(image_name), frame,
                                       n_frames, loopType);
    animations.put(anim_name, new_anim);
  }
}
