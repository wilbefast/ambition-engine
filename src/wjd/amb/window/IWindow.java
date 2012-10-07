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

import wjd.math.V2;
import wjd.amb.model.Scene;

/**
 * @author wdyce
 * @since 1 Aug, 2012
 */
public interface IWindow
{
  /* NESTING */
  public abstract class TimeManager
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
  /* CONSTANTS */
  public static final int MAX_FPS = 60;

  /* ACCESSORS */
  /**
   * How big is the Window?
   *
   * @return the vector size of the Window in pixels.
   */
  public V2 getSizeV2();
  
    /**
   * Return the current time.
   *
   * @return the current system time in milliseconds.
   */
  public long timeNow();

  /* LIFE CYCLE */
  /**
   * Create the IWindow and OpenGL canvas based on the size and other parameters
   * that were given to the constructor.
   *
   * @param name the String of characters to be displayed at the top of the
   * IWindow (NB - the name java is still used to identify the process itself).
   * @param width the width of the IWindow, in pixels.
   * @param height the height of the IWindow, in pixels.
   * @throws an Exception if there's or problem: this is optional.
   */
  public void create(String name, V2 size, Scene scene) throws Exception;

  /**
   * Launch the application and run until some event interrupts its execution.
   */
  public abstract void run();

  /**
   * Clean up anything we might have allocated.
   */
  public void destroy();
}
