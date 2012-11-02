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
package wjd.amb.control;

/**
 *
 * @author wdyce
 * @since Nov 1, 2012
 */
public class Controller implements IInteractive
{
  /* METHODS */

  /**
   * Treat a single KeyPress event.
   *
   * @param event a KeyPress event that has occurred.
   * @return the result of the update, normally CONTINUE.
   */
  public EUpdateResult processKeyPress(IInput.KeyPress event)
  {
    // override if need be
    return EUpdateResult.CONTINUE;
  }

  /**
   * Treat a single MouseClick event.
   *
   * @param event a MouseClick event that has occurred.
   * @return the result of the update, normally CONTINUE.
   */
  public EUpdateResult processMouseClick(IInput.MouseClick event)
  {
    // override if need be
    return EUpdateResult.CONTINUE;
  }

  /* IMPLEMENTS -- IDYNAMIC */
  
  @Override
  public EUpdateResult processInput(IInput input)
  {
    // override if need be
    return EUpdateResult.CONTINUE;
  }
}
