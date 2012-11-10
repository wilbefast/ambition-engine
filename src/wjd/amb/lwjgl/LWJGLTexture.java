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
package wjd.amb.lwjgl;

import org.newdawn.slick.opengl.Texture;
import wjd.amb.resources.ATexture;
import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class LWJGLTexture extends ATexture
{
  /* ATTRIBUTES */
  private Texture slick_texture;
  private V2 size, fraction_used;

  /* METHODS */
  
  // constructors
  LWJGLTexture(Texture slick_texture)
  {
    this.slick_texture = slick_texture;
    // NB - Slick fills textures to the nearest power of 2!
    size = new V2(slick_texture.getImageWidth(), slick_texture.getImageHeight());
    fraction_used = new V2(slick_texture.getWidth(), slick_texture.getHeight());
  }
  
  // accessors
  public V2 getUsedFraction()
  {
    return fraction_used;
  }
  
  // mutators
  public void bind()
  {
    slick_texture.bind();
  }
  
  /* IMPLEMENTS -- ATEXTURE */
  
  @Override
  public V2 getSize()
  {
    return size;
  }
  

}
