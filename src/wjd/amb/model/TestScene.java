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
package wjd.amb.model;

import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.amb.control.IInput.KeyPress;
import wjd.amb.control.IInput.MouseClick;
import wjd.amb.view.ICanvas;
import wjd.math.V2;

/**
 * An IScene in which agent go about the business.
 *
 * @author wdyce
 * @since Sep 27, 2012
 */
public class TestScene extends Scene
{
  /* CONSTANTS */
  private static final V2 HELLO_POS = new V2(100, 100);
  private static final String HELLO_TEXT = "Hello Agents!";
  
  /* METHODS */
  // construction
  public TestScene()
  {
    // start up
    reset();
  }

  private void reset()
  {
  }

  // overrides
  @Override
  public EUpdateResult update(int t_delta)
  {
    // all clear !
    return EUpdateResult.CONTINUE;
  }

  @Override
  public void render(ICanvas canvas)
  {
    if(!canvas.isCameraActive())
      canvas.createCamera(null);
    
    // clear the screen
    canvas.clear();

    // draw hello text
    canvas.text(HELLO_TEXT, HELLO_POS);
  }

  @Override
  public EUpdateResult processStaticInput(IInput input)
  {
    // exit if the escape key is pressed
    if(input.isKeyHeld(IInput.EKeyCode.ESC))
      return EUpdateResult.STOP;
    else
      return EUpdateResult.CONTINUE;
  }

  @Override
  public EUpdateResult processKeyPress(KeyPress event)
  {
    return EUpdateResult.CONTINUE;
  }

  @Override
  public EUpdateResult processMouseClick(MouseClick event)
  {
    return EUpdateResult.CONTINUE;
  }
}
