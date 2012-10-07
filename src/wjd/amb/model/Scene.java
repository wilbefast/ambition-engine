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
import wjd.amb.control.IDynamic;
import wjd.amb.control.IInput;
import wjd.amb.control.IInteractive;
import wjd.amb.view.IVisible;

/**
 * The Scene is the content that is display by the Window: it is possible to
 * switch from Scene to Scene over the course of the game.
 *
 * @author wdyce
 * @since 06-Oct-2012
 */
public abstract class Scene implements IDynamic, IVisible, IInteractive
{
  /* ATTRIBUTES */
  /**
   * The Scene to switch to after this one.
   */
  protected Scene next = null;

  /* METHODS */
  
  /**
   * It's time to switch to a new Scene, but which?
   *
   * @return the next Scene that the Window will display, allocated but the
   * current Scene.
   */
  public Scene getNext()
  {
    return next;
  }

  /* ABSTRACT METHODS */

  /**
   * Process the static part of the input, that is the key- and mouse-button
   * states, not the state-change events.
   *
   * @param input the IInput object containing key-states.
   * @return
   */
  public abstract EUpdateResult processStaticInput(IInput input);

  /**
   * Treat a single KeyPress event.
   *
   * @param event a KeyPress event that has occurred.
   * @return the result of the update, normally CONTINUE.
   */
  public abstract EUpdateResult processKeyPress(IInput.KeyPress event);

  /**
   * Treat a single MouseClick event.
   *
   * @param event a MouseClick event that has occurred.
   * @return the result of the update, normally CONTINUE.
   */
  public abstract EUpdateResult processMouseClick(IInput.MouseClick event);

  /* IMPLEMENTATIONS -- IINTERACTIVE */
  @Override
  public final EUpdateResult processInput(IInput input)
  {
    IInput.Event e;
    while ((e = input.pollEvents()) != null)
      // event is a keypress
      if (e instanceof IInput.KeyPress)
      {
        EUpdateResult result = processKeyPress((IInput.KeyPress) e);
        if (result != EUpdateResult.CONTINUE)
          return result;
      }
      // event is a mouse-click
      else if (e instanceof IInput.MouseClick)
      {
        EUpdateResult result = processMouseClick((IInput.MouseClick) e);
        if (result != EUpdateResult.CONTINUE)
          return result;
      }

    // all the events are dealt with, now there's just the static state
    return processStaticInput(input);
  }
}
