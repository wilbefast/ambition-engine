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
import wjd.amb.control.AWTInput;
import wjd.amb.view.AWTCanvas;
import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 2, 2012
 */
public class AWTWindowJFrame  extends JFrame
{
  /* ATTRIBUTES */
  
  /* METHODS */

  // constructors
  public AWTWindowJFrame(String name, V2 size, AWTCanvas awtCanvas, 
                                               AWTInput awtInput)
  {
    // open window
    setTitle(name);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize((int)size.x, (int)size.y);
    setResizable(false);
    setLocationRelativeTo(null);    // move to center of screen
    // set content
    setContentPane(awtCanvas);
    // set input
    addKeyListener(awtInput);
    addMouseListener(awtInput);
    addMouseMotionListener(awtInput);
    addMouseWheelListener(awtInput);
    // This should always be last
    setVisible(true);
  }
  
  public void destroy()
  {
    // close the window
    WindowEvent e = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }
}
