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
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.amb.model.Scene;
import wjd.amb.model.TestScene;
import wjd.amb.view.ICanvas;
import wjd.amb.window.AWTWindow;
import wjd.amb.window.IWindow;
import wjd.amb.window.LWJGLWindow;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 07-Oct-2012
 */
public abstract class AmbitionEngine
{
  /* NESTING */
  public static abstract class TimeManager
  {
    // attributes
    private static long t_previous = -1; // -1 => uninitialised

    /**
     * Return the amount of time since the method was last called.
     *
     * @param t_now the current system time in milliseconds.
     * @return the current time in milliseconds since this method was last
     * called, or 0 the first time the method is called.
     */
    public static int getDelta(long t_now)
    {
      int t_delta = (t_previous < 0) ? 0 : (int) (t_now - t_previous);
      t_previous = t_now;
      return t_delta;
    }
  }
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
  public static void mainLoop(IWindow window, String window_name, V2 window_size,
                         Scene first_scene) throws Exception
  {
    // start up
    window.create(window_name, window_size, first_scene);
    
    // cache references
    Scene scene = window.getCurrentScene();
    ICanvas canvas = window.getCanvas();
    IInput input = window.getInput();

    // run
    boolean running = true;
    while (running)
    {
      // update -- model
      int t_delta = TimeManager.getDelta(window.timeNow());
      if (canvas.processInput(input) == EUpdateResult.STOP
      || scene.processInput(input) == EUpdateResult.STOP
      || scene.update(t_delta) == EUpdateResult.STOP)
      {
        // change to new Scene if a new one if offered
        Scene next = scene.getNext();
        if (next != null)
          window.setScene(next);
        // exit otherwise
        else
          running = false;
      }
      // update -- view
      window.refreshDisplay();
      
      // leave some time for other processes
      window.sleep();
    }

    // shut down
    window.destroy();
  }

  public static void launch(String window_name, V2 window_size,
                            Scene first_scene)
  {
    /* NB - LWJGL uses native libraries, so this program will crash at mainLoop-time
     * unless you indicate to the JVM where to find them! As such the program
     * must be mainLoop with the following argument: 
     * -Djava.library.path=/a/path/to/lwjgl-2.8.4/native/your_operating_system
     */
    IWindow window = null;
    try
    {
      // by default try to create a window using LWJGL's native OpenGL
      LOGGER.log(Level.INFO, "Launching LWJGL Window");
      mainLoop(window = new LWJGLWindow(), window_name, window_size, first_scene);

    }
    catch (UnsatisfiedLinkError | LWJGLException lwjgl_ex)
    {
      try
      {
        // You probably forgot to use -Djava.library.path=...
        LOGGER.log(Level.WARNING, lwjgl_ex.toString(), lwjgl_ex);
        // default to AWT if there's a problem with LWJGL
        LOGGER.log(Level.INFO, "Launching AWT Window");
        mainLoop(window = new AWTWindow(), window_name, window_size, first_scene);
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

  /* SIMPLE TEST */
  public static void main(String[] args)
  {
    /* NB - LWJGL uses native libraries, so this program will crash at mainLoop-time
     * unless you indicate to the JVM where to find them! As such the program
     * must be mainLoop with the following argument: 
     * -Djava.library.path=/a/path/to/lwjgl-2.8.4/native/your_operating_system
     */
    AmbitionEngine.launch("test", new V2(640, 480), new TestScene());
  }
}
