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

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import wjd.math.V2;


  /** 
* @author wdyce
* @since 01-Oct-2012
*/
public class LWJGLInput implements IInput
{
  /* FUNCTIONS */
  
  private static int bridgeKeyEvent(EKeyCode code)
  {
    switch(code)
    {
      case L_SHIFT: return Keyboard.KEY_LSHIFT;
      case L_CTRL: return Keyboard.KEY_LCONTROL;
      case L_ALT: return Keyboard.KEY_LMENU;
      case SPACE: return Keyboard.KEY_SPACE;
      case R_ALT: return Keyboard.KEY_RMENU;
      case R_CTRL: return Keyboard.KEY_RCONTROL;
      case R_SHIFT: return Keyboard.KEY_RSHIFT;
      case ENTER: return Keyboard.KEY_RETURN;
      case ESC: return Keyboard.KEY_ESCAPE;
      case BACKSPACE: return Keyboard.KEY_BACK;
      default: return Keyboard.KEY_NONE;
    }
  }
  
  private static EKeyCode bridgeKeyEvent(int code)
  {
    switch(code)
    {
      case Keyboard.KEY_LSHIFT : return EKeyCode.L_SHIFT;
      case Keyboard.KEY_LCONTROL : return EKeyCode.L_CTRL;
      case Keyboard.KEY_LMENU : return EKeyCode.L_ALT;
      case Keyboard.KEY_SPACE : return EKeyCode.SPACE;
      case Keyboard.KEY_RMENU : return EKeyCode.R_ALT;
      case Keyboard.KEY_RCONTROL : return EKeyCode.R_CTRL;
      case Keyboard.KEY_RSHIFT : return EKeyCode.R_SHIFT;
      case Keyboard.KEY_RETURN : return EKeyCode.ENTER;
      case Keyboard.KEY_ESCAPE : return EKeyCode.ESC;
      case Keyboard.KEY_BACK : ; 
      default: return null;
    }
  }
  
  private static int bridgeMouseEvent(EMouseButton code)
  {
    return code.ordinal();
  }
  
  private static EMouseButton bridgeMouseEvent(int code)
  {
    switch(code)
    {
      case 0: return EMouseButton.LEFT;
      case 1: return EMouseButton.MIDDLE;
      case 2: return EMouseButton.RIGHT;
      default: return null;
    }
  }
  
  /* SINGLETON */
  
  private static LWJGLInput instance = null;
  
  public static LWJGLInput getInstance() throws LWJGLException
  {
    if(instance == null)
      instance = new LWJGLInput();
    return instance;
  }
  
  /* ATTRIBUTES */
  private V2 key_direction = new V2(),
             mouse_position = new V2();
  private Event nextMouseEvent;
  private Event nextKeyEvent;
  private int window_height; // needed to invert mouse coordinates
  
  
  /* METHODS */
  
  // constructors
  private LWJGLInput() throws LWJGLException
  {
    // LWJGL - Keyboard
    Keyboard.create();
    Keyboard.enableRepeatEvents(false);
    // LWJGL - Mouse
    Mouse.setGrabbed(false);
    Mouse.create();
  }
  
  // accessors
  public void setWindowHeight(int window_height)
  {
    this.window_height = window_height;
  }
  
  /* IMPLEMENTATIONS -- IINPUT */
  
  @Override
  public int getMouseWheelDelta()
  {
    return Mouse.getDWheel();
  }

  @Override
  public V2 getMousePosition()
  {
    mouse_position.xy(Mouse.getX(), window_height - Mouse.getY());
    return mouse_position;
  }

  @Override
  public V2 getKeyDirection()
  {
    // Reset key direction vector
    key_direction.xy(0, 0);
    
    // Update keyboard direction vector
    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
      key_direction.yadd(1);
    if (Keyboard.isKeyDown(Keyboard.KEY_UP))
      key_direction.yadd(-1);
    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
      key_direction.xadd(1);
    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
      key_direction.xadd(-1);
    
    // Return the vector
    return key_direction;
  }

  @Override
  public boolean isKeyHeld(EKeyCode code)
  {
    int lwjgl_code = bridgeKeyEvent(code);
    return (lwjgl_code != Keyboard.KEY_NONE)
      ? Keyboard.isKeyDown(lwjgl_code)
      : false;
  }

  @Override
  public boolean isMouseClicking(EMouseButton button)
  {
    return Mouse.isButtonDown(button.ordinal());
  }

  @Override
  public Event pollEvents()
  {
    // get a new event of each type
    if(nextMouseEvent == null)
      nextMouseEvent = nextMouseClick();
    if(nextKeyEvent == null)
      nextKeyEvent = nextKeyPress();
    
    // choose whether to return a Mouse or Keyboard event
    Event nextEvent;
    if(nextMouseEvent == null && nextKeyEvent == null)
      nextEvent = null;
    else if (nextMouseEvent != null && nextKeyEvent == null)
      nextEvent = nextMouseEvent;
    else if (nextKeyEvent != null && nextMouseEvent == null)
      nextEvent = nextKeyEvent;
    else // if (nextKeyEvent != null && nextMouseEvent != null)
    {
      // choose the oldest Event
      nextEvent = (nextMouseEvent.t_stamp < nextKeyEvent.t_stamp)
                  ? nextMouseEvent : nextKeyEvent;
    }
    
    // don't return the same event twice!
    if(nextEvent == nextKeyEvent) 
      nextKeyEvent = null;
    else if(nextEvent == nextMouseEvent)
      nextMouseEvent = null;
    
    // all done !
    return nextEvent;
  }
/* SUBROUTINES */
  
  public KeyPress nextKeyPress()
  {
    return (Keyboard.next())
      ? new KeyPress(Keyboard.getEventNanoseconds()/1000, 
                    bridgeKeyEvent(Keyboard.getEventKey()),
                    Keyboard.getEventKeyState())
      : null;
  }
  
  public MouseClick nextMouseClick()
  {
    while(Mouse.next())
    {
      if(Mouse.getEventButton() != -1)
        return new MouseClick(Mouse.getEventNanoseconds()/1000, 
                              bridgeMouseEvent(Keyboard.getEventKey()),
                              Mouse.getEventButtonState());
    }
    return null;
  }
}
