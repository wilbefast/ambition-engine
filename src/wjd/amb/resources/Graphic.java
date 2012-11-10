/*
 Copyright (C) 2012 William James Dyce

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
