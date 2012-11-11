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
package wjd.amb.awt;

import java.awt.image.BufferedImage;
import wjd.amb.resources.ITexture;
import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class AWTTexture implements ITexture
{
  /* ATTRIBUTES */
  private BufferedImage awt_texture;
  private V2 size;

  /* METHODS */
  
  // constructors
  AWTTexture(BufferedImage awt_texture)
  {
    this.awt_texture = awt_texture;
    size = new V2(awt_texture.getWidth(), awt_texture.getHeight());
  }

  /* IMPLEMENTS -- ITEXTURE */
  
  @Override
  public V2 getSize()
  {
    return size;
  }

  BufferedImage getImage()
  {
    return awt_texture;
  }
  

}
