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

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import wjd.amb.AScene;
import wjd.amb.AWindow;
import wjd.math.V2;

/**
 * Basic Light-weight Java Game Library (LWJGL) holder class which creates a
 * IWindow and OpenGL canvas we can use to draw on.
 *
 * @author wdyce
 * @since 16-Feb-2012
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class LWJGLWindow extends AWindow
{
  /* METHODS */
  
  public LWJGLWindow(String _name, V2 _size, AScene first_scene)
  {
    super(_name, _size, first_scene);
  }
  
  /* IMPLEMENTATION -- AWINDOW */

  /**
   * Return the current time.
   *
   * @return the current system time in milliseconds using LWJGL.
   */
  @Override
  public long timeNow()
  {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }

  /**
   * Create a LWJGL Display of the given size, with a corresponding OpenGL 
   * canvas.
   *
   * @param name the String of characters to be displayed at the top of the
   * IWindow (NB - the name java is still used to identify the process itself).
   * @param width the width of the IWindow, in pixels.
   * @param height the height of the IWindow, in pixels.
   * @throws LWJGLException if native libraries are not found, graphics card or
   * drivers do not support hardware rendering...
   */
  @Override
  public void create() throws LWJGLException
  {
    // LWJGL - Display
    Display.setDisplayMode(new DisplayMode((int)size.x, (int)size.y));
    Display.setTitle(name);
    Display.setFullscreen(false);
    Display.setVSyncEnabled(true);
    Display.setResizable(true);
    Display.create();
    // view
    canvas = LWJGLCanvas.getInstance(); // must be after Display initialisation!
    canvas.setSize(size);
    // control
    input = LWJGLInput.getInstance();
    ((LWJGLInput)input).setWindowHeight((int)size.y);
  }
  
  /**
   * Clean up anything we might have allocated.
   */
  @Override
  public void destroy()
  {
    // these methods already check if created before destroying
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }
  
  @Override
  public void refreshDisplay(AScene scene)
  {
    // check if window was resized
    if (Display.wasResized())
    {
      size.xy(Display.getWidth(), Display.getHeight());
      canvas.setSize(size);
      ((LWJGLInput)input).setWindowHeight((int)size.y);
    }
    
    // check if window is in focus
    if (Display.isVisible())
    {
      // render if this window has the focus
      scene.render(canvas);
    }
    else
    {
      // render anyway if the the display has been corrupted somehow
      if (Display.isDirty())
        scene.render(canvas);
      // update less often if out of focus
      try 
      { 
        Thread.sleep(100); 
      }
      catch (InterruptedException ex) 
      { 
        // do nothing if interrupted
      }
    }
      
    // swap buffers
    Display.update();
  }
  
  /**
   * Leave some time for other processes.
   */
  @Override
  public void sleep()
  {
    Display.sync(MAX_FPS);
  }
}
