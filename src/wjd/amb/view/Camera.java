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

import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.amb.control.IInteractive;
import wjd.math.Rect;
import wjd.math.V2;

/**
 * A Camera that can be panned with the keyboard or mouse, and zoomed towards a
 * specific target (say, the mouse) so that the target is kept in the same place
 * relative to the view, as in Google Maps.
 *
 * @author wdyce
 * @since 05-Mar-2012
 */
public class Camera implements IInteractive
{
  /* CONSTANTS */
  private static final int SCOLL_MOUSE_DISTANCE = 48;
  private static final int SCROLL_SPEED = 6;
  private static final float ZOOM_SPEED = 0.001f;
  private static final float ZOOM_MIN = 0.1f;
  private static final float ZOOM_MAX = 2.0f;
  private static final float ZOOM_DEFAULT = 1.0f;
  
  /* ATTRIBUTES */
  private V2 canvas_size;
  private Rect view, boundary;
  private float zoom;

  /* METHODS */
  // creation
  /**
   * Create a camera by specifying the size of the canvas its perspective will
   * be drawn to as well as the boundary rectangle the view should remain
   * within.
   *
   * @param canvas_size the vector size in pixels of the area of the screen that
   * the camera's view will be projected on to.
   * @param boundary the Rectangle that the view must remain within, or null for
   * no boundary.
   */
  public Camera(V2 canvas_size, Rect boundary)
  {
    this.canvas_size = canvas_size;
    this.boundary = boundary;
    view = new Rect(V2.ORIGIN, canvas_size);
    zoom = ZOOM_DEFAULT;
  }

  /**
   * Reset the position and zoom of the Camera.
   */
  public void reset()
  {
    view.reset(V2.ORIGIN, canvas_size);
    zoom = ZOOM_DEFAULT;
  }

  // query
  /**
   * Return the amount of zoom, the real factor by which all visible objects'
   * sizes we be multiplied.
   *
   * @return the amount of zoom between <a href="ViewPort#ZOOM_MIN">ZOOM_MIN</a>
   * and <a href="ViewPort#ZOOM_MAX">ZOOM_MAX</a>.
   */
  public float getZoom()
  {
    return zoom;
  }

  /**
   * Convert a position relative to the world origin (for instance, an agent)
   * into a position relative to the view.
   *
   * @param position the vector position to convert.
   * @return a new vector position corresponding to the position of the
   * specified point relative to the view.
   */
  public V2 getPerspective(V2 position)
  {
    return new V2((position.x() - view.x()) * zoom, (position.y() - view.y())
      * zoom);
  }

  /**
   * Convert a position relative to the view (for instance, the position of the
   * mouse cursor) into a position relative to the world origin.
   *
   * @param position the vector position to convert.
   * @return a new vector position corresponding to the absolute position of the
   * specified point.
   */
  public V2 getGlobal(V2 position)
  {
    return new V2(position.x() / zoom + view.x(), position.y() / zoom + view.y());
  }

  /**
   * Return true if the specified position is in view, false if not.
   *
   * @param position the vector point to check, relative to the world origin not
   * the Window.
   * @return true if the position is inside the view Rectangle.
   */
  public boolean containsPoint(V2 position)
  {
    return view.contains(position);
  }
  
  /** Which cells of the specified grid are in view?
   * 
   * @param min a vector reference wherein will be written the coordinates of 
   * the minimum grid cell.
   * @param max a vector reference wherein will be written the coordinates of 
   * the maximum grid cell. 
   * @param cell_size the size of each square cell.
   */
  public void getVisibleGridCells(V2 min, V2 max, V2 grid_size, float cell_inv_size)
  {
    // top-left cell
    min.xy((float)Math.max(0, Math.floor(view.x()*cell_inv_size)), /* col */
          (float)Math.max(0, Math.floor(view.y()*cell_inv_size))); /* row */
    
    // bottom-right cell
    max.xy((float)Math.min(grid_size.y(), Math.ceil(min.x() /* col */
                        + view.w()*cell_inv_size) + 1),
          (float)Math.min(grid_size.x(), Math.ceil(min.y() /* row */
                        + view.h()*cell_inv_size) + 1)); 

  }

  // modification
  /**
   * Change the size of the canvas this view is to manage: this could
   * theoretically be subsection of the application Window, though there isn't
   * as yet support for this (currently one canvas takes up the whole Window).
   *
   * @param canvas_size the new size of the canvas.
   */
  public void setCanvasSize(V2 canvas_size)
  {
    this.canvas_size = canvas_size;
    view.size(canvas_size.clone().scale(1.0f / zoom));
  }

  /**
   * Pan the view at a fixed height (zoom) level: the pan speed depends on the
   * level of zoom (the closer we are the faster we move).
   *
   * @param translation a vector direction to move the view in.
   */
  public void pan(V2 translation)
  {
    // move the view
    view.shift(translation.scale(1 / zoom));

    // don't stray out of bounds
    if (boundary != null)
      keepInsideBounds();
  }

  /**
   * Zoom towards or away from the specified target.
   *
   * @param delta amount to zoom: negative moves us away from the target,
   * positive towards it, and speed depends on the zoom we already have.
   * @param target vector position on the screen to move towards or away from.
   */
  public void zoom(float delta, V2 target)
  {
    V2 target_true = getGlobal(target);
    V2 target_relative = new V2(canvas_size.x() / target.x(),
      canvas_size.y() / target.y());

    // reset zoom counter, don't zoom too much
    zoom += delta * zoom;
    if (zoom > ZOOM_MAX)
      zoom = ZOOM_MAX;
    else if (zoom < ZOOM_MIN)
      zoom = ZOOM_MIN;

    // perform the zoom
    view.size(canvas_size.clone().scale(1.0f / zoom));
    view.x(target_true.x() - view.w() / target_relative.x());
    view.y(target_true.y() - view.h() / target_relative.y());

    // don't stray out of bounds
    if (boundary != null)
      keepInsideBounds();
  }
  
  /* IMPLEMENTATIONS */
  @Override
  public EUpdateResult processInput(IInput input, V2 window_size)
  {
    EUpdateResult result = processKeyboard(input);
    if(result != EUpdateResult.CONTINUE)
      return result;
    else
      return processMouse(input, window_size);
    
  }

  /* SUBROUTINES */
  private void keepInsideBounds()
  {
    V2 overlap = view.overlap(boundary);

    // pan view to keep within borders

    // pan view to keep within borders -- left/right
    if (overlap.x() < 0)
    {
      // left
      if (view.x() < boundary.x())
        view.x(boundary.x());
      // right
      else if (view.endx() > boundary.endx())
        view.x(boundary.endx() - view.w());
    }
    else if (overlap.x() > 0)
      view.x(boundary.x() - overlap.x() * 0.5f);

    // pan view to keep within borders -- top/bottom
    if (overlap.y() < 0)
    {
      // top
      if (view.y() < boundary.y())
        view.y(boundary.y());
      // bottom
      else if (view.endy() > boundary.endy())
        view.y(boundary.endy() - view.h());
    }
    else if (overlap.y() > 0)
      view.y(boundary.y() - overlap.y() * 0.5f);
  }
  
  private EUpdateResult processKeyboard(IInput input)
  {
    // move the camera
    pan(input.getKeyDirection().scale(SCROLL_SPEED));
    
    // keep on keeping on :)
    return EUpdateResult.CONTINUE;
  }
  
  private EUpdateResult processMouse(IInput input, V2 window_size)
  {
    // mouse position
    V2 mouse_pos = input.getMousePosition(window_size); 

    // mouse near edges = pan
    /*V2 scroll_dir = new V2();
    if (mouse_pos.x() < SCOLL_MOUSE_DISTANCE)
      scroll_dir.x(-1);
    else if (mouse_pos.x() > window_size.x() - SCOLL_MOUSE_DISTANCE)
      scroll_dir.x(1);
    if (mouse_pos.y() < SCOLL_MOUSE_DISTANCE)
      scroll_dir.y(-1);
    else if (mouse_pos.y() > window_size.y() - SCOLL_MOUSE_DISTANCE)
      scroll_dir.y(1);
    pan(scroll_dir.scale(SCROLL_SPEED));*/

    // mouse wheel = zoom
    int wheel = input.getMouseWheelDelta();
    if (wheel != 0)
      zoom(wheel * ZOOM_SPEED, mouse_pos);
    
    // keep on keeping on :)
    return EUpdateResult.CONTINUE;
  }
  
}
