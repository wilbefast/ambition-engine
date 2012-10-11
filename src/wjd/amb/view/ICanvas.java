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
package wjd.amb.view;

import wjd.amb.control.IInteractive;
import wjd.math.Rect;
import wjd.math.V2;

/**
 * A collection of abstract drawing functions to render primitives (circles,
 * boxes and so-on).
 *
 * @author wdyce
 * @since Aug 1, 2012
 */
public interface ICanvas extends IInteractive
{
  // query
  /**
   * What Camera does this Canvas draw its contents based on, if any?
   *
   * @return the attached Camera, or null if there is none.
   */
  public Camera getCamera();

  /**
   * Does this canvas draw shapes relative to a Camera view.
   *
   * @return true if the camera is active, false otherwise.
   */
  public boolean isCameraActive();

  // modify the canvas itself
  /**
   * The size of the canvas container has changed: update the size of the canvas
   * to make up for this.
   *
   * @param size the new size of the canvas.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas setSize(V2 size);

  /**
   * The Camera defines what part of the Scene is viewed by the Canvas.
   *
   * @param camera the new Camera to be used.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas setCamera(Camera camera);

  /**
   * The Camera defines what part of the Scene is viewed by the Canvas.
   *
   * @param camera the new Camera to be used.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas createCamera(Rect boundary);

  // modify the paintbrush state
  /**
   * Set the Colour to be applied to all future drawing operations.
   *
   * @param colour the new Colour to be used to draw objects from now on.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas setColour(Colour colour);

  /**
   * Set the line width to be used for all future drawing operations.
   *
   * @param colour the new Colour to be used to draw objects from now on.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas setLineWidth(float lineWidth);

  /**
   * Set the text font to be used for all future drawing operations.
   *
   * @param font the new font Object to be used.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas setFont(Object font);

  /**
   * We'll want to turn off the Camera for GUI elements.
   *
   * @param use_camera true to use the camera, false to not use it.
   * @return a reference to this, so multiple operations can be queued.
   */
  public ICanvas toggleCamera(boolean use_camera);

  // drawing functions
  /**
   * Clear the screen.
   */
  public void clear();

  /**
   * Draw a circle outline around the specified position, using the given
   * radius.
   *
   * @param centre vector position corresponding to the centre of the circle.
   * @param radius the size of the circle from centre to outskirts.
   */
  public void circle(V2 centre, float radius);

  /**
   * Draw a straight line between the two specified points.
   *
   * @param start vector position corresponding to the start of the line.
   * @param end vector position corresponding to the end of the line.
   */
  public void line(V2 start, V2 end);

  /**
   * Draw the outline of a Rectangle.
   *
   * @param rect the rectangle object whose outline will be drawn.
   */
  public void box(Rect rect);

  /**
   * Draw a String of characters at the indicated position in black.
   *
   * @param string the String of characters to be drawn.
   * @param position the position on the screen to draw the String.
   */
  public void text(String string, V2 position);
}