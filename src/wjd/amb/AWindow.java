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

import org.lwjgl.opengl.Display;
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IDynamic;
import wjd.amb.control.IInput;
import wjd.amb.view.ICanvas;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 1 Aug, 2012
 */
public abstract class AWindow implements IDynamic
{
  /* CONSTANTS */
  public static final int MAX_FPS = 60;
  
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

  
  /* ATTRIBUTES */
  // concrete
  protected String name;
  protected V2 size;
  // abstract
  protected ICanvas canvas;
  protected IInput input;
  protected AScene scene;
  
  /* METHODS */
  
  /**
   * Pass the required attributes to the window but don't open it yet.
   * 
   * @param name Name of the Window, to be displayed at the top.
   * @param size The vector size of the Window in pixels: (width, height).
   */
  public AWindow(String name, V2 size, AScene first_scene)
  {
    this.name = name;
    this.size = size.floor();
    this.scene = first_scene;
  }
  
  /**
   * Run the application until it's time to stop.
   */
  public void run() throws Exception
  {
    // start up
    create();
   

    // run
    boolean running = true;
    while (running)
    {
      // update -- model
      int t_delta = TimeManager.getDelta(timeNow());
      if (this.update(t_delta) == EUpdateResult.STOP
      || scene.processInput(input) == EUpdateResult.STOP
      || scene.update(t_delta) == EUpdateResult.STOP)
      {
        // change to new Scene if a new one if offered
        AScene next = scene.getNext();
        if (next != null)
          scene = next;
        // exit otherwise
        else
          running = false;
      }
      // update -- view
      refreshDisplay(scene);
      
      // leave some time for other processes
      sleep();
    }

    // shut down
    destroy();
  }
  
  /* IMPLEMENTS -- IDYNAMIC */

  @Override
  public EUpdateResult update(int t_delta)
  {
    // can be overridden if needed...
    return EUpdateResult.CONTINUE;
  }
  
  /* INTERFACE */
  
  /**
   * Return the current time.
   *
   * @return the current system time in milliseconds.
   */
  public abstract long timeNow();

  /* LIFE CYCLE */

  public abstract void create() throws Exception;
  
  /**
   * Clean up anything we might have allocated and close the Window.
   */
  public abstract void destroy();
  
  /**
   * Redraw the contents of the screen.
   * 
   * @param scene the Scene to be drawn onto the Window's Canvas.
   */
  public abstract void refreshDisplay(AScene scene);
  
  /**
   * Leave some time for other processes.
   */
  public abstract void sleep();
}