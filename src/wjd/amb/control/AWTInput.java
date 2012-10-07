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

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import wjd.math.V2;
import wjd.util.LimitedQueue;

/**
 * Adaptor interface to deal with input coming from either LWJGL or AWT.
 *
 * @author wdyce
 * @since 25 Jan, 2012
 */
public class AWTInput implements IInput, KeyListener, MouseListener,
  MouseMotionListener, MouseWheelListener
{ 
  /* NESTING */
  
  private static class Mouse
  {
    // constants
    public static final int WHEEL_DELTA_MULTIPLIER = -180;
    
    // mouse buttons are set out: left, middle, right
    public boolean clicking[] =
    {
      false, false, false
    };
    public V2 position = new V2(0, 0);
    public double last_scroll = 0.0;

  }

  private static class Keyboard
  {
    public boolean pressing[] =
    {
      false, false, false, false, false, false, false, false, false, false, 
      false, false, false, false
    };
    // overall direction of arrow-key movement
    public V2 direction = new V2(0, 0);
  }
  
  /* FUNCTIONS */
  private EKeyCode bridgeKeyEvent(KeyEvent e)
  {
    switch(e.getKeyCode())
    {
      // arrow keys
      case KeyEvent.VK_UP: return EKeyCode.UP;
      case KeyEvent.VK_DOWN: return EKeyCode.DOWN;
      case KeyEvent.VK_LEFT: return EKeyCode.LEFT;
      case KeyEvent.VK_RIGHT: return EKeyCode.RIGHT;

      // keys with location
      case KeyEvent.VK_SHIFT: 
        return (e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) 
                                    ? EKeyCode.L_SHIFT : EKeyCode.R_SHIFT;
      case KeyEvent.VK_CONTROL: 
        return (e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) 
                                    ? EKeyCode.L_CTRL : EKeyCode.R_CTRL;
      case KeyEvent.VK_ALT: 
        return (e.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) 
                                    ? EKeyCode.L_ALT : EKeyCode.R_ALT;
      // absolute keys
      case KeyEvent.VK_SPACE: return EKeyCode.SPACE;
      case KeyEvent.VK_ENTER: return EKeyCode.ENTER;
      case KeyEvent.VK_ESCAPE: return EKeyCode.ESC;
        
      // unknown
      default: return null;
    }
  }
  
  /* CONSTANTS */
  private static final int MAX_EVENTS = 10;
  
  
  /* SINGLETON */
  
  private static AWTInput instance;

  public static AWTInput getInstance()
  {
    if (instance == null)
      instance = new AWTInput();
    return instance;
  }
  
  /* ATTRIBUTES */
  
  Mouse mouse;
  Keyboard keyboard;
  LimitedQueue<IInput.Event> events 
    = new LimitedQueue<IInput.Event>(MAX_EVENTS);
  
  /* METHODS */
  
  // creation
  private AWTInput()
  {
    mouse = new Mouse();
    keyboard = new Keyboard();
    // No phantom KeyReleased Events on Linux please!
    KeyRepeatFix.install();
  }
  
  /* IMPLEMENTATIONS - IINPUT */
  
  @Override
  public int getMouseWheelDelta()
  {
    int delta = (int)(mouse.last_scroll * Mouse.WHEEL_DELTA_MULTIPLIER);
    mouse.last_scroll = 0.0;
    return delta;
  }
  
  @Override
  public V2 getKeyDirection()
  {
    // Reset key direction vector
    keyboard.direction.xy(0, 0);
    
    // Update keyboard direction vector
    if (keyboard.pressing[EKeyCode.DOWN.ordinal()])
      keyboard.direction.yadd(1);
    if (keyboard.pressing[EKeyCode.UP.ordinal()])
      keyboard.direction.yadd(-1);
    if (keyboard.pressing[EKeyCode.RIGHT.ordinal()])
      keyboard.direction.xadd(1);
    if (keyboard.pressing[EKeyCode.LEFT.ordinal()])
      keyboard.direction.xadd(-1);
    
    // Return the vector
    return keyboard.direction;
  }

  @Override
  public boolean isKeyHeld(EKeyCode code)
  {
    return keyboard.pressing[code.ordinal()];
  }

  @Override
  public boolean isMouseClicking(EMouseButton button)
  {
    return mouse.clicking[button.ordinal()];
  }

  @Override
  public V2 getMousePosition()
  {
    return mouse.position;
  }
  
  @Override
  public Event pollEvents()
  {
    return events.poll();
  }
  
  /* IMPLEMENTATIONS - KEYLISTENER */

  @Override
  public void keyTyped(KeyEvent e)
  { /* unused */ }

  @Override
  public void keyPressed(KeyEvent e)
  {
    EKeyCode code = bridgeKeyEvent(e);
    
    // no key repeats!
    if(isKeyHeld(code))
      return;
    
    events.add(new KeyPress(System.currentTimeMillis(), bridgeKeyEvent(e), true));
    setKeyState(e, true);
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    events.add(new KeyPress(System.currentTimeMillis(), bridgeKeyEvent(e), false));
    setKeyState(e, false);
  }

  
  /* IMPLEMENTATIONS - MOUSELISTENER */
  
  @Override
  public void mouseClicked(MouseEvent e)
  { /* unused */ }

  @Override
  public void mouseEntered(MouseEvent e)
  { /* unused */ }

  @Override
  public void mouseExited(MouseEvent e)
  { /* unused */ }

  @Override
  public void mousePressed(MouseEvent e)
  {
    mouse.clicking[e.getButton() - 1] = true;
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    mouse.clicking[e.getButton() - 1] = false;
  }

  
  /* IMPLEMENTATIONS - MOUSEMOTIONLISTENER */

  @Override
  public void mouseDragged(MouseEvent e)
  { /* unused */ }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    Point p = e.getPoint();
    mouse.position.xy(p.x, p.y);
  }
  
  /* IMPLEMENTATIONS - MOUSEWHEELLISTENER */
  
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    mouse.last_scroll = e.getWheelRotation();
  }

  
  /* SUBROUTINES */
  
  private void setKeyState(KeyEvent e, boolean new_state)
  {
    EKeyCode code = bridgeKeyEvent(e);
    if(code != null)
      keyboard.pressing[code.ordinal()] = new_state;
  }
}
