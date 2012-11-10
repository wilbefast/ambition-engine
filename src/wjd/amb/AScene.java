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
import wjd.amb.control.IController;
import wjd.amb.control.IDynamic;
import wjd.amb.control.IInput;
import wjd.amb.view.ICanvas;
import wjd.amb.view.IVisible;

/**
 * The Scene is the content that is display by the Window: it is possible to
 * switch from Scene to Scene over the course of the game.
 *
 * @author wdyce
 * @since 06-Oct-2012
 */
public abstract class AScene implements IDynamic, IVisible, IController
{
  /* ATTRIBUTES */
  /**
   * The Scene to switch to after this one.
   */
  protected AScene next = null;
  protected IController controller = this;
  protected AWindow window = null;

  /* METHODS */
  
  // constructors
  
  // accessors
  /**
   * It's time to switch to a new Scene, but which?
   *
   * @return the next Scene that the Window will display, allocated but the
   * current Scene.
   */
  public AScene getNext()
  {
    return next;
  }
  
  /**
   * @return the Scene's window.
   */
  public AWindow getWindow()
  {
    return window;
  }
  
  // mutators
  
  public void initialise(AWindow window, IInput input, ICanvas canvas)
  {
    this.window = window;
    
    // overrides if nessecary
  }
  
  public void setNext(AScene next)
  {
    this.next = next;
  }
  
  /**
   * The same Scene object can be controlled in different ways: we can implement
   * IController to treat input events for the Scene.
   * 
   * @param controller the new IController to be used to manipulate this Scene.
   */
  public void setController(IController controller)
  {
    this.controller = controller;
  }
  
  public void setWindow(AWindow window)
  {
    this.window = window;
  }
  
  /* IMPLEMENTS -- ICONTROLLER */
  
  @Override
  public EUpdateResult processKeyPress(IInput.KeyPress event)
  {
    // override if needed
    return EUpdateResult.CONTINUE;
  }

  @Override
  public EUpdateResult processMouseClick(IInput.MouseClick event)
  {
    // override if needed
    return EUpdateResult.CONTINUE;
  }

  /* IMPLEMENTATS -- IINTERACTIVE */
  @Override
  public EUpdateResult processInput(IInput input)
  {
    IInput.Event e;
    while ((e = input.pollEvents()) != null)
    {
      // event is a keypress
      if (e instanceof IInput.KeyPress)
      {
        EUpdateResult result = controller.processKeyPress((IInput.KeyPress) e);
        if (result != EUpdateResult.CONTINUE)
          return result;
      }
      // event is a mouse-click
      else if (e instanceof IInput.MouseClick)
      {
        EUpdateResult result = controller.processMouseClick((IInput.MouseClick) e);
        if (result != EUpdateResult.CONTINUE)
          return result;
      }
    }
    // all the events are dealt with, now there's just the static state
    return (controller != this 
            ? controller.processInput(input) 
            : EUpdateResult.CONTINUE);
  }
}
