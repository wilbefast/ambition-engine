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
package wjd.amb.window;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.amb.control.LWJGLInput;
import wjd.amb.model.Scene;
import wjd.amb.view.GLCanvas;
import wjd.math.V2;

/**
 * Basic Light-weight Java Game Library (LWJGL) holder class which creates a
 * IWindow and OpenGL canvas we can use to draw on.
 *
 * @author wdyce
 * @since 16-Feb-2012
 * @see <a href="http://lwjgl.org/">LWJGL Home Page</a>
 */
public class LWJGLWindow implements IWindow
{
  /* ATTRIBUTES */
  // window
  private V2 size;
  // model
  private Scene scene;
  // view
  private GLCanvas glCanvas;
  // control
  private IInput input;

  /* METHODS */
  
  /**
   * How big is the Window?
   *
   * @return the vector size of the Window in pixels.
   */
  @Override
  public V2 getSizeV2()
  {
    return size;
  }
  
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
  
  /* IMPLEMENTATION -- IWINDOW */

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
  public void create(String name, V2 size, Scene scene) throws LWJGLException
  {
    // window
    this.size = size.floor();
    // LWJGL - Display
    Display.setDisplayMode(new DisplayMode((int)size.x(), (int)size.y()));
    Display.setFullscreen(false);
    Display.setTitle(name);
    Display.create();
    Display.setResizable(true);
    // model
    this.scene = scene;
    // view
    glCanvas = GLCanvas.getInstance(); // must be after Display initialisation!
    // control
    input = LWJGLInput.getInstance();

    // OpenGL
    resizeGL();
  }

  // launch and update
  /**
   * Launch the application and run until some event interrupts its execution.
   */
  @Override
  public void run()
  {
    boolean running = true;
    while(running)
    {
      // don't update if display is not in focus
      if (Display.isVisible())
      {
        // update
        if(scene.processInput(input, size)  == EUpdateResult.STOP
        || scene.update(TimeManager.getDelta(timeNow())) == EUpdateResult.STOP)
        {
          // change to new Scene if a new one if offered
          Scene next = scene.getNext();
          if(next != null)
            scene = next;
          // exit otherwise
          else
            running = false;
        }
        // check for window events
        processWindow();
        // render
        scene.render(glCanvas, null);
      }
      else
      {
        // redraw screen if out of date
        if (Display.isDirty())
          scene.render(glCanvas, null);
        try
        {
          Thread.sleep(100);
        }
        catch (InterruptedException ex)
        {
          // Thread interrupted by another
        }
      }
      Display.update();
      Display.sync(MAX_FPS);
      
      // check whether we should stop running
      if(Display.isCloseRequested())
        running = false;
    }
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
  
  /* SUBROUTINES */

  /**
   * Resize the OpenGL canvas.
   */
  private void resizeGL()
  {
    //Here we are using a 2D Scene
    glViewport(0, 0, (int)size.x(), (int)size.y());
    // projection
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, size.x(), size.y(), 0, -1, 1);
    glPushMatrix();
    // model view
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
    
    // resize camera viewport too
    scene.processWindowResize(new V2((int)size.x(), (int)size.y()));
  }

  /**
   * Treat any IWindow events that might has occurred, for instance minimisation
   * or resizing of the IWindow.
   */
  private void processWindow()
  {
    // check if window was resized
    if (Display.wasResized())
    {
      size.xy(Display.getWidth(), Display.getHeight());
      resizeGL();
    }
  }
}
