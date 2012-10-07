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

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.lwjgl.LWJGLException;
import wjd.amb.control.AWTInput;
import wjd.amb.control.EUpdateResult;
import wjd.math.V2;
import wjd.amb.model.Scene;
import wjd.amb.view.AWTCanvas;

/** 
 * Application using IWindow AWT: only use this version if your computer does not 
 * support hardware accelerated graphics!
 * 
 * @author wdyce 
 * @since 25 Jan, 2012
 */
public class AWTWindow extends JFrame implements IWindow
{
  /* CONSTANTS */

  /* ATTRIBUTES */
  // window
  private V2 size;
  // model
  private Scene scene;
  // view
  private AWTCanvas canvas;
  // control
  private AWTInput input;

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
   * Return the current time using the standard Java System method.
   *
   * @return the current system time in milliseconds using LWJGL.
   */
  @Override
  public long timeNow()
  {
    return System.currentTimeMillis();
  }
  

  /* IMPLEMENTATIONS -- IWINDOW */
  
  /**
   * Create the AWT JFrame IWindow of the given size, with a corresponding JPanel 
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
  public void create(String name, V2 size, Scene scene)
  {
    // window
    this.size = size;
    // set up AWT window
    setTitle(name);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize((int)size.x(), (int)size.y());
    setResizable(false);
    setLocationRelativeTo(null);    // move to center of screen
    // model 
    this.scene = scene;
    // view
    canvas = new AWTCanvas();
    setContentPane(canvas);
    // control
    input = AWTInput.getInstance();
    addKeyListener(input);
    addMouseListener(input);
    addMouseMotionListener(input);
    addMouseWheelListener(input);
    // This should always be last
    setVisible(true);
  }

  /**
   * Launch the application and run until some event interrupts its execution.
   */
  @Override
  public void run()
  {
    // Run until told to stop
    boolean running = true;
    while(running)
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
      // queue rendering
      scene.render(canvas, null); // use the Scene's own camera where applicable
      repaint();

      // Catch exceptions
      try
      {
        // Leave some time for other threads
        Thread.sleep(1000/MAX_FPS);
      } 
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    // End of the loop : close the window
    WindowEvent e = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }
  
  /**
   * Clean up anything we might have allocated.
   */
  @Override
  public void destroy()
  {
  }
}
