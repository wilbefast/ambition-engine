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
import wjd.amb.control.IInput;
import wjd.amb.model.Scene;
import wjd.amb.view.AWTCanvas;
import wjd.amb.view.ICanvas;
import wjd.math.V2;

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
  private AWTCanvas awtCanvas;
  // control
  private AWTInput awtInput;

  /* IMPLEMENTATION -- IWINDOW */
  
  @Override
  public Scene getCurrentScene()
  {
    return scene;
  }

  @Override
  public ICanvas getCanvas()
  {
    return awtCanvas;
  }
  
  @Override
  public IInput getInput()
  {
    return awtInput;
  }
  
  @Override
  public IWindow setScene(Scene scene)
  {
    this.scene = scene;
    return this;
  }
  
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
    awtCanvas = new AWTCanvas();
    setContentPane(awtCanvas);
    // control
    awtInput = AWTInput.getInstance();
    addKeyListener(awtInput);
    addMouseListener(awtInput);
    addMouseMotionListener(awtInput);
    addMouseWheelListener(awtInput);
    // This should always be last
    setVisible(true);
  }
  
  @Override
  public void destroy()
  {
    // close the window
    WindowEvent e = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }
  
  @Override
  public void refreshDisplay()
  {
    // queue rendering
    scene.render(awtCanvas);
    repaint();
  }
  
  /**
   * Leave some time for other processes.
   */
  @Override
  public void sleep()
  {
    try
    {
      Thread.sleep(1000/MAX_FPS);
    } 
    catch (InterruptedException e)
    {
      // do nothing
    }
  }
}
