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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import wjd.amb.control.IInput;
import wjd.amb.control.KeyRepeatFix;
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

  /* FUNCTIONS */
  
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
  
  /* NESTING */
  
  private static class Mouse
  {
    // constants
    public static final int WHEEL_DELTA_MULTIPLIER = -225;
    
    // mouse buttons are set out: left, middle, right
    public boolean clicking[] =
    {
      false, false, false
    };
    public V2 position = new V2(0, 0), 
              movement = new V2(0, 0);
    public double last_scroll = 0.0;
    public Robot robot;
    public boolean capture;
    private boolean robot_controlled = false;
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
      // alphanumeric keys
      case KeyEvent.VK_0:
      case KeyEvent.VK_1:
      case KeyEvent.VK_2:
      case KeyEvent.VK_3:
      case KeyEvent.VK_4:
      case KeyEvent.VK_5:
      case KeyEvent.VK_6:
      case KeyEvent.VK_7:
      case KeyEvent.VK_8:
      case KeyEvent.VK_9:
      case KeyEvent.VK_A:
      case KeyEvent.VK_B:
      case KeyEvent.VK_C:
      case KeyEvent.VK_D:
      case KeyEvent.VK_E:
      case KeyEvent.VK_F:
      case KeyEvent.VK_G:
      case KeyEvent.VK_H:
      case KeyEvent.VK_I:
      case KeyEvent.VK_J:
      case KeyEvent.VK_K:
      case KeyEvent.VK_L:
      case KeyEvent.VK_M:
      case KeyEvent.VK_N:
      case KeyEvent.VK_O:
      case KeyEvent.VK_P:
      case KeyEvent.VK_Q:
      case KeyEvent.VK_R:
      case KeyEvent.VK_S:
      case KeyEvent.VK_T:
      case KeyEvent.VK_U:
      case KeyEvent.VK_V:
      case KeyEvent.VK_W:
      case KeyEvent.VK_X:
      case KeyEvent.VK_Y:
      case KeyEvent.VK_Z:
        return EKeyCode.ALPHANUMERIC;
        
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
  
  private Mouse mouse;
  private Keyboard keyboard;
  private LimitedQueue<IInput.Event> events 
    = new LimitedQueue<IInput.Event>(MAX_EVENTS);
  private V2 win_centre_abs = new V2(), win_centre_rel = new V2();
  
  /* METHODS */
  
  // creation
  private AWTInput()
  {
    // Mouse
    mouse = new Mouse();
    try
    {
      mouse.robot = new Robot();
    }
    catch (AWTException ex)
    {
      Logger.getLogger(AWTInput.class.getName()).log(Level.SEVERE, null, ex);
      mouse.robot = null;
    }
    
    // Keyboard
    keyboard = new Keyboard();
    // No phantom KeyReleased Events on Linux please!
    KeyRepeatFix.install();
  }
  
  // mutators
  
  public void grabCursor(boolean toggle)
  {
    mouse.capture = toggle;
  }
  
  public void setWindowCentre(V2 absolute, V2 offset)
  {
    win_centre_abs.reset(absolute);
    win_centre_rel.reset(absolute).sub(offset);
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
      keyboard.direction.y += 1;
    if (keyboard.pressing[EKeyCode.UP.ordinal()])
      keyboard.direction.y -= 1;
    if (keyboard.pressing[EKeyCode.RIGHT.ordinal()])
      keyboard.direction.x += 1;
    if (keyboard.pressing[EKeyCode.LEFT.ordinal()])
      keyboard.direction.x -= 1;
    
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
    return (button == EMouseButton.ANY)
      ? (mouse.clicking[0] || mouse.clicking[1] || mouse.clicking[2])
      : mouse.clicking[button.ordinal()];
  }

  @Override
  public V2 getMousePosition()
  {
    return mouse.position;
  }
  
  @Override
  public V2 getMouseMove()
  {
    V2 result = mouse.movement.clone();
    mouse.movement.xy(0.0f, 0.0f);
    return result;
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
    
    events.add(new KeyPress(System.currentTimeMillis(), this, 
                bridgeKeyEvent(e), true));
    setKeyState(e, true);
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    events.add(new KeyPress(System.currentTimeMillis(), this, 
              bridgeKeyEvent(e), false));
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
    int button_i = e.getButton() - 1;
    events.add(new MouseClick(System.currentTimeMillis(), this, 
                bridgeMouseEvent(button_i), true));
    mouse.clicking[button_i] = true;
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    int button_i = e.getButton() - 1;
    events.add(new MouseClick(System.currentTimeMillis(), this, 
                bridgeMouseEvent(button_i), false));
    mouse.clicking[button_i] = false;
  }

  
  /* IMPLEMENTATIONS - MOUSEMOTIONLISTENER */

  @Override
  public void mouseDragged(MouseEvent e)
  { 
    mouseMoved(e);
  }

  @Override
  public void mouseMoved(MouseEvent e)
  {
    // get new position
    Point p = e.getPoint();
    
    // ignore robot events
    if(mouse.robot_controlled 
       || (p.x == mouse.position.x && p.y == mouse.position.y))
    {
      mouse.robot_controlled = false;
      return;
    }
    
    mouse.movement.xy(p.x, p.y).sub((mouse.capture) ? win_centre_rel : mouse.position);
    mouse.position.xy(p.x, p.y);
    
    if(mouse.capture)
    {
      mouse.robot.mouseMove((int)win_centre_abs.x, (int)win_centre_abs.y);
      mouse.position.reset(win_centre_rel);
      mouse.robot_controlled = true;
    }
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
