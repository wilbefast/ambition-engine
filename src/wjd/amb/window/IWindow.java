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

import wjd.amb.control.IInput;
import wjd.amb.model.AScene;
import wjd.amb.view.ICanvas;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 1 Aug, 2012
 */
public interface IWindow
{
  /* CONSTANTS */
  public static final int MAX_FPS = 60;
  
  /* ACCESSORS */
  
  /** Get the Canvas object.
   * 
   * @return the Canvas object this Window uses.
   */
  public ICanvas getCanvas();
  
  /** Get the Input object.
   * 
   * @return the Input object this Window uses.
   */
  public IInput getInput();
  
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
  public void create(String name, V2 size) throws Exception;

  /**
   * Clean up anything we might have allocated and close the Window.
   */
  public void destroy();
  
  /**
   * Redraw the contents of the screen.
   * 
   * @param scene the Scene to be drawn onto the Window's Canvas.
   */
  public void refreshDisplay(AScene scene);
  
  /**
   * Leave some time for other processes.
   */
  public void sleep();
}
