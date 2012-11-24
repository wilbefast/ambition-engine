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

import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IDynamic;
import wjd.amb.control.IInput;
import wjd.amb.resources.AAudioManager;
import wjd.amb.resources.ATextureManager;
import wjd.amb.resources.IResourceLoader;
import wjd.amb.view.ICanvas;
import wjd.math.V2;

/**
 * @author wdyce
 * @since 1 Aug, 2012
 */
public abstract class AWindow implements IDynamic
{
  /* CONSTANTS */
  public static final int MAX_FPS = 30;

  
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
  protected boolean fullscreen;
  // abstract
  protected ICanvas canvas;
  protected IInput input;
  protected AScene scene;
  protected ATextureManager textureManager;
  protected AAudioManager audioManager;
  
  /* METHODS */
  
  // constructors
  
  /**
   * Pass the required attributes to the window but don't open it yet.
   * 
   * @param name Name of the Window, to be displayed at the top.
   * @param size The vector size of the Window in pixels: (width, height).
   * @param textureManager The object used to load and store graphics.
   * @param audioManager The object used to load and store audio clips.
   * if not.
   */
  public AWindow(String name, V2 size, AScene first_scene, 
                ATextureManager textureManager, AAudioManager audioManager)
  {
    // window name
    this.name = name;
    
    // window size, fullscreen
    this.size = size;
    fullscreen = (size == null);
    if(fullscreen)
      size = desktopResolution();
    else
      size.floor();
    
    // model
    scene = first_scene;
    
    // resources
    this.textureManager = textureManager;
    this.audioManager = audioManager;
  }
  
  // accessors
  
  public V2 getSize()
  {
    return size;
  }
  
  public ATextureManager getTextureManager()
  {
    return textureManager;
  }
  
  // update
  
  /**
   * Run the application until it's time to stop.
   * @param loader the resource-loading script to be run during the 
   * initialisation.
   */
  public void run(IResourceLoader loader) throws Exception
  {
    // start up
    create();
    
    // load resources *after* startup
    if(loader != null)
      loader.load(textureManager, audioManager);
    scene.initialise(this, input, canvas);
   
    // run
    boolean running = true;
    while (running)
    {
      // update -- model
      int t_delta = TimeManager.getDelta(timeNow());
      if (this.update(t_delta) != EUpdateResult.CONTINUE
      || scene.processInput(input) != EUpdateResult.CONTINUE
      || scene.update(t_delta) != EUpdateResult.CONTINUE)
      {
        // change to new Scene if a new one if offered
        AScene next = scene.getNext();
        if (next != null)
        {
          scene = next;
          scene.initialise(this, input, canvas);
        }
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
   * Does this application claim the mouse?
   * 
   * @param toggle true to hide the mouse and to keep it within the window,
   * false to release and start redrawing the mouse.
   */
  public abstract void grabCursor(boolean toggle);
  
  /**
   * Return the current time.
   *
   * @return the current system time in milliseconds.
   */
  public abstract long timeNow();
  
  /**
   * What is the resolution of the user's desktop?
   * 
   * @return the current screen width and height in vector form.
   */
  public abstract V2 desktopResolution();

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
