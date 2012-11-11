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
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import wjd.amb.resources.ATextureManager;
import wjd.amb.resources.ITexture;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class AWTTextureManager extends ATextureManager
{

  /* IMPLEMENTS -- ARESOURCEMANAGER */
  
  @Override
  protected ITexture loadTexture(String filename, ImageFileType type)
  {
    try
    {
      AWTTexture result 
        = new AWTTexture(ImageIO.read(new File(filename)));
      return result;
    }
    catch (IOException ex)
    {
      Logger.getLogger(AWTTextureManager.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }
}
