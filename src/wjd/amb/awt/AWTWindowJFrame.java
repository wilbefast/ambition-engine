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
package wjd.amb.awt;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 2, 2012
 */
public class AWTWindowJFrame  extends JFrame
{
  /* ATTRIBUTES */
  private V2 centre = new V2(), offset = new V2();
  
  /* METHODS */

  // constructors
  public AWTWindowJFrame(String name, V2 size, AWTCanvas awtCanvas, 
                                               AWTInput awtInput)
  {
    // open window
    setTitle(name);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize((int)size.x, (int)size.y);
    setResizable(true);
    setLocationRelativeTo(null);    // move to center of screen
    // set content
    setContentPane(awtCanvas);
    // set input
    addKeyListener(awtInput);
    awtCanvas.addMouseListener(awtInput);
    awtCanvas.addMouseMotionListener(awtInput);
    awtCanvas.addMouseWheelListener(awtInput);
    
    // This should always be last of all the AWT commands
    setVisible(true);
    
    //awtInput.setOffset();
    
    /*int w = getWidth(), inner_w = getContentPane().getWidth();
    System.out.println("location: " + this.getLocation());
    System.out.println("content pane location: " + this.getContentPane().getLocation());
    
    System.out.println("frame width : "+getWidth());
    System.out.println("frame height: "+getHeight());
    System.out.println("content pane width : "+getContentPane().getWidth());
    System.out.println("content pane height: "+getContentPane().getHeight());
    System.out.println("width  of left + right  borders: "+(getWidth()-getContentPane ().getWidth()));
    System.out.println("height of top  + bottom borders: "+(getHeight()-getContentPane().getHeight()));*/


  }
  
  // accessors
  
  public V2 getCentre()
  {
    Point corner = getLocationOnScreen();
    Dimension size = getSize();
    return (centre.xy(corner.x + size.width/2, corner.y + size.height/2));
  }
  
  public V2 getOffset()
  {
    Point corner = getLocationOnScreen();
    return (offset.xy(corner.x, corner.y));
  }
  
  // mutators
  
  public void destroy()
  {
    // close the window
    WindowEvent e = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }
}
