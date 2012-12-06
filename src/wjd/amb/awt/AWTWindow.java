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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.MemoryImageSource;
import wjd.amb.AScene;
import wjd.amb.AWindow;
import wjd.math.V2;

/** 
 * Application using IWindow AWT: only use this version if your computer does not 
 * support hardware accelerated graphics!
 * 
 * @author wdyce 
 * @since 25 Jan, 2012
 */
public class AWTWindow extends AWindow implements ComponentListener
{
  /* ATTRIBUTES */
  private AWTWindowJFrame jframe;
  
  /* METHODS */
  
  public AWTWindow(String _name, V2 _size, AScene first_scene)
  {
    super(_name, _size, first_scene, new AWTTextureManager(), new AWTAudioManager()); 
    
    if(size == null)
      size = desktopResolution();
  }

  /* IMPLEMENTATION -- AWINDOW */
  
  @Override
  public void grabCursor(boolean toggle)
  {
    // grab or release the cursor
    ((AWTInput)input).grabCursor(toggle);
    
    // hide the cursor
    if(toggle)
    {
      int[] pixels = new int[16 * 16];
      Image image = Toolkit.getDefaultToolkit().createImage(
              new MemoryImageSource(16, 16, pixels, 0, 16));
      Cursor transparentCursor = Toolkit.getDefaultToolkit().createCustomCursor
                                    (image, new Point(0, 0), "invisibleCursor");
      
      jframe.getContentPane().setCursor(transparentCursor);
    }
  }
  
  @Override
  public long timeNow()
  {
    return System.currentTimeMillis();
  }
  
  @Override
  public V2 desktopResolution()
  {
    Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    return new V2(d.width, d.height);
  }

  @Override
  public void create()
  {
    // view
    canvas = new AWTCanvas();
    canvas.setSize(size);
    // control
    input = AWTInput.getInstance();
    // finally create AWT window
    jframe = new AWTWindowJFrame(name, size, (AWTCanvas)canvas, (AWTInput)input);
    jframe.addComponentListener(this);
  }
  
  @Override
  public void destroy()
  {
    jframe.destroy();
  }
  
  @Override
  public void refreshDisplay(AScene scene)
  {
    // queue rendering
    scene.render(canvas);
    // launch rendering pass
    jframe.repaint();
  }
  
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

  /* IMPLEMENTS -- COMPONENTLISTENER */
  
  @Override
  public void componentResized(ComponentEvent ce)
  {
    resetWindowSize();
  }

  @Override
  public void componentMoved(ComponentEvent ce)
  {
    resetWindowSize();
  }

  @Override
  public void componentShown(ComponentEvent ce) { }

  @Override
  public void componentHidden(ComponentEvent ce) { }
  
  /* SUBROUTINES */
  
  public void resetWindowSize()
  {
    canvas.setSize(new V2(jframe.getWidth(), jframe.getHeight()));
    ((AWTInput)input).setWindowCentre(jframe.getCentre(), jframe.getOffset());
  }
}
