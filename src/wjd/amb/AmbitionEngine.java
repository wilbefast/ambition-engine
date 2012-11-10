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
package wjd.amb;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import wjd.amb.awt.AWTWindow;
import wjd.amb.lwjgl.LWJGLWindow;
import wjd.amb.resources.IResourceLoader;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 07-Oct-2012
 */
public abstract class AmbitionEngine
{

  /* CLASS NAMESPACE CONSTANTS */
  public static final Logger LOGGER = Logger.getLogger(AmbitionEngine.class.getName());

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
      // by default try to create a window using LWJGL's native OpenGL
      LOGGER.log(Level.INFO, "Launching LWJGL Window");
      
      // create window
      (window = new LWJGLWindow(window_name, window_size, first_scene)).run(loader);
    }
    catch (UnsatisfiedLinkError | LWJGLException lwjgl_ex)
    {
      try
      {
        // You probably forgot to use -Djava.library.path=...
        LOGGER.log(Level.WARNING, lwjgl_ex.toString(), lwjgl_ex);
        // default to AWT if there's a problem with LWJGL
        LOGGER.log(Level.INFO, "Launching AWT Window");
        
        // create window
        (window = new AWTWindow(window_name, window_size, first_scene)).run(loader);
      }
      catch (Exception awt_ex)
      {
        // A generic error caused by the code, not the library
        LOGGER.log(Level.SEVERE, awt_ex.toString(), awt_ex);
      }
    }
    catch (Exception ex)
    {
      // A generic error caused by the code, not the library
      LOGGER.log(Level.SEVERE, ex.toString(), ex);
    }
    finally
    {
      // Window can be safely destroy here, as AWT should have replace LWJGL
      if (window != null)
        window.destroy();
    }
  }
}
