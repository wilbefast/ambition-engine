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
public class AWTWindow extends AWindow
{
  /* ATTRIBUTES */
  private AWTWindowJFrame jframe;
  
  /* METHODS */
  
  public AWTWindow(String _name, V2 _size, AScene first_scene)
  {
    super(_name, _size, first_scene, null, null); // FIXME
  }

  /* IMPLEMENTATION -- AWINDOW */
  
  @Override
  public long timeNow()
  {
    return System.currentTimeMillis();
  }
  
  @Override
  public V2 screenSize()
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
}
