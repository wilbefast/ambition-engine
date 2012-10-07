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

import wjd.math.V2;

/**
 * Adaptor interface to deal with input coming from either LWJGL or AWT.
 *
 * @author wdyce
 * @since 01-Oct-2012
 */
public interface IInput
{
  /* NESTING */
  public static enum EKeyCode
  {
    UP, RIGHT, DOWN, LEFT, L_SHIFT, L_CTRL, L_ALT, SPACE, R_ALT, R_CTRL,
    R_SHIFT, ENTER, ESC, BACKSPACE
  }
  public static enum EMouseButton
  {
    LEFT, MIDDLE, RIGHT
  }
  public static class Event
  {
    public long t_stamp;
    public Event(long t_stamp)
    {
      this.t_stamp = t_stamp;
    }
  }
  public static class KeyPress extends Event
  {
    public EKeyCode key;
    public boolean state;
    public KeyPress(long t_stamp, EKeyCode key, boolean state)
    {
      super(t_stamp);
      this.key = key;
      this.state = state;
    }
  }
  public static class MouseClick extends Event
  {
    public EMouseButton button;
    public boolean state;
    public MouseClick(long t_stamp, EMouseButton button, boolean state)
    {
      super(t_stamp);
      this.button = button;
      this.state = state;
    }
  }

  /* METHODS */
  /**
   * Get the current variation of the mouse wheel.
   *
   * @return the mouse wheel delta.
   */
  public int getMouseWheelDelta();

  /**
   * Get the current position of the cursor.
   *
   * @return the vector position of the mouse.
   */
  public V2 getMousePosition(V2 window_size);

  /**
   * Get the direction that the user is pressing using a combination of arrow
   * keys.
   *
   * @return the vector direction of the keyboard arrow-keys, where the
   * horizontal and vertical components are each either -1, 0 or 1.
   */
  public V2 getKeyDirection();

  /**
   * Check whether a given key is currently being pressed on not.
   *
   * @param code the key the state of which we wish to learn.
   * @return true if the specified key is being pressed, false otherwise.
   */
  public boolean isKeyHeld(EKeyCode code);

  /**
   * Check whether a given mouse button is currently being pressed on not.
   *
   * @param button the button the state of which we wish to learn.
   * @return true if the specified button is being pressed, false otherwise.
   */
  public boolean isMouseClicking(EMouseButton button);
  
  /**
   * Get the next input event.
   */
  public Event pollEvents();
}