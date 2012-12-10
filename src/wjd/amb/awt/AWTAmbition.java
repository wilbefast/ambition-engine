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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import wjd.amb.AScene;
import wjd.amb.AWindow;
import wjd.amb.resources.IResourceLoader;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 07-Oct-2012
 */
public abstract class AWTAmbition
{

  /* CLASS NAMESPACE CONSTANTS */
  public static final Logger LOGGER = Logger.getLogger(AWTAmbition.class.getName());

  /* CLASS INITIALISATION */
  static
  {
    try
    {
      // externalise logs if possible
      LOGGER.addHandler(new FileHandler("ambition-engine.log", true));
    }
    catch (IOException ex)
    {
      // warning if not
      LOGGER.log(Level.WARNING, ex.toString(), ex);
    }
  }
  
  /* FUNCTIONS */

  public static void launch(String window_name, V2 window_size,
                            AScene first_scene, IResourceLoader loader)
  {
    /* NB - LWJGL uses native libraries, so this program will crash at mainLoop-time
     * unless you indicate to the JVM where to find them! As such the program
     * must be mainLoop with the following argument: 
     * -Djava.library.path=/a/path/to/lwjgl-2.8.4/native/your_operating_system
     */
    AWindow window = null;
    try
    {
      // create window
      (window = new AWTWindow(window_name, window_size, first_scene)).run(loader);
    }
    catch (Exception awt_ex)
    {
      // A generic error caused by the code, not the library
      LOGGER.log(Level.SEVERE, awt_ex.toString(), awt_ex);
    }
    finally
    {
      // Window can be safely destroy here, as AWT should have replace LWJGL
      if (window != null)
        window.destroy();
    }
  }
}
