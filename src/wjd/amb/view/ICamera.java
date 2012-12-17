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
import wjd.math.Circle;
import wjd.math.Rect;
import wjd.math.V2;

/**
 * An abstract Camera instance that is used to view distort the view of the
 * ICanvas, finding certain objects or moving them around.
 *
 * @author wdyce
 * @since 05-Mar-2012
 */
public interface ICamera extends IInteractive
{
  /* ACCESSORS */
  
  /**
   * Return the amount of zoom, the real factor by which all visible objects'
   * sizes we be multiplied: the default is thus 1 where no zoom is applicable.
   *
   * @return the amount of zoom.
   */
  public float getZoom();

  /**
   * Convert a position relative to the world origin (for instance, an agent)
   * into a position relative to the view.
   *
   * @param position the vector position to convert.
   * @return a new vector position corresponding to the position of the
   * specified point relative to the view.
   */
  public V2 getPerspective(V2 position);

  /**
   * Convert a rectangle relative to the world origin (for instance, an agent's
   * hit-box) into a rectangle relative to the view.
   *
   * @param rect the rectangle to convert.
   * @return a new vector position corresponding to a new version of the  
   * rectangle relative to the view.
   */
  public Rect getPerspective(Rect rect);

  /**
   * Convert a position relative to the view (for instance, the position of the
   * mouse cursor) into a position relative to the world origin.
   *
   * @param position the vector position to convert.
   * @return a new vector position corresponding to the absolute position of the
   * specified point.
   */
  public V2 getGlobal(V2 position);
  
  /**
   * Convert a position relative to the view (for instance, the position of the
   * mouse cursor) into a position relative to the world origin.
   *
   * @param position the vector position to convert.
   * @return a new vector position corresponding to the absolute position of the
   * specified point.
   */
  public Rect getGlobal(Rect rect);

  /**
   * Return true if the specified position is in view, false if not.
   *
   * @param position the vector point to check, relative to the world origin not
   * the camera.
   * @return true if the position is visible to the camera.
   */
  public boolean canSee(V2 position);
  
  /**
   * 
   * 
   * @param area the area to check, relative to the world origin and not the 
   * camera.
   * @return true if any part of the area is visible to the camera.
   */
  public boolean canSee(Rect area);
  
  /**
   * 
   * 
   * @param area the circle to check, relative to the world origin and not the 
   * camera.
   * @return true if any part of the circle is visible to the camera.
   */
  public boolean canSee(Circle circle);
  

  /* MUTATORS */
  
  /**
   * Reset the ICamera to its default configuration.
   */
  public void reset();
  
  /**
   * Change the portion of the canvas this camera is to project to: this 
   * could theoretically be any subsection of the application Window.
   *
   * @param projection_area the new projection target.
   */
  public void setProjectionArea(Rect projection_area);
  
  /**
   * Change the size of the canvas portion this camera is to project to: the 
   * projection area is assumed to start in the top-left (0,0).
   * 
   *
   * @param projection_size the new size of the projection target.
   */
  public void setProjectionSize(V2 projection_size);

  /**
   * Pan (shift) the view following a vector's direction and intensity.
   *
   * @param translation a vector direction to move the view in.
   */
  public void pan(V2 translation);
  
  /**
   * Centre the camera over a new position.
   * 
   * @param new_position the vector to use to reset the camera's position.
   */
  public void setPosition(V2 new_position);

  /**
   * Zoom towards or away from the specified target.
   *
   * @param delta amount to zoom: negative moves us away from the target,
   * positive towards it, and speed depends on the zoom we already have.
   * @param target vector position on the screen to move towards or away from.
   */
  public void zoom(float delta, V2 target);
}
