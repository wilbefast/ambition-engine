package wjd.amb.resources;

import java.awt.image.BufferedImage;
import wjd.math.Rect;

/**
 * 
 * @author wjd
 * @since jan-2012
 */
public class Graphic
{
  /// ATTRIBUTES

  protected BufferedImage image;
  protected Rect frame;

  /// METHODS
  // creation
  public Graphic(BufferedImage init_image, Rect init_frame)
  {
    image = init_image;
    frame = init_frame;
  }

  // query
  public BufferedImage getImage()
  {
    return image;
  }

  public Rect getFrame()
  {
    return frame;
  }
}
