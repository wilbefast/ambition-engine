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

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class LWJGLTexture extends ATexture
{
  /* ATTRIBUTES */
  Texture slick_texture;

  /* METHODS */
  
  // constructors
  LWJGLTexture(Texture slick_texture)
  {
    this.slick_texture = slick_texture;
  }
  
  // mutators
  public void bind()
  {
    slick_texture.bind();
  }

}
