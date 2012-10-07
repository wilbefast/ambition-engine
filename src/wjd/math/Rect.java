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

package wjd.math;

/**
 * A 2D Rectangle class, mostly useful for views and collision-testing between
 * box-shaped objects (this Rectangle does not rotate though).
 *
 * @author wdyce
 * @since 24 Aug, 2012
 */
public class Rect
{
  /* CONSTANTS */

  /* FUNCTIONS */

  /* ATTRIBUTES */
  /**
   * The horizontal offset.
   */
  private float x;
  /**
   * The vertical offset.
   */
  private float y;
  /**
   * The width.
   */
  private float w;
  /**
   * The height.
   */
  private float h;

  /* METHODS */
  // constructors
  /**
   * Create a null rectangle (no length or width) at the origin.
   */
  public Rect()
  {
    x = y = w = h = 0.0f;
  }

  /**
   * Create a rectangle with the specified offset (position) and size.
   *
   * @param x the horizontal offset.
   * @param y the vertical offset.
   * @param w the width.
   * @param h the height.
   */
  public Rect(float x, float y, float w, float h)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  /**
   * Create a rectangle with the specified vector offset (position) and size.
   *
   * @param position the position vector: (x, y).
   * @param size the size vector: (w, h).
   */
  public Rect(V2 position, V2 size)
  {
    x = position.x();
    y = position.y();
    w = size.x();
    h = size.y();
  }

  // accessors
  /**
   * Get the horizontal position of the Rectangle.
   *
   * @return the horizontal offset.
   */
  public float x()
  {
    return x;
  }

  /**
   * Get the vertical position of the Rectangle.
   *
   * @return the vertical offset.
   */
  public float y()
  {
    return y;
  }

  /**
   * Get the horizontal position of the bottom-right corner of the rectangle.
   *
   * @return the horizontal offset of the bottom-right corner: x + w.
   */
  public float endx()
  {
    return x + w;
  }

  /**
   * Get the vertical position of the bottom-right corner of the rectangle.
   *
   * @return the vertical offset of the bottom-right corner: y + h.
   */
  public float endy()
  {
    return y + h;
  }

  /**
   * Get the width of the Rectangle.
   *
   * @return the width.
   */
  public float w()
  {
    return w;
  }

  /**
   * Get the height of the Rectangle.
   *
   * @return the height.
   */
  public float h()
  {
    return h;
  }

  /**
   * Get the vector position of the Rectangle.
   *
   * @return the offset of the Rectangle (x, y).
   */
  public V2 pos()
  {
    return new V2(x, y);
  }

  /**
   * Get the vector size of the Rectangle.
   *
   * @return the size of the Rectangle (w, h).
   */
  public V2 size()
  {
    return new V2(w, h);
  }

  /**
   * Get the width-to-height ratio of the Rectangle.
   *
   * @return the width-to-height ratio of the Rectangle (w / h).
   */
  public float ratio()
  {
    return w / h;
  }

  /**
   * Convert to String, for debugging.
   *
   * @return a String formatted as follows: (x, y, w, h)
   */
  @Override
  public String toString()
  {
    return ("(" + x + ',' + y + ',' + w + ',' + h + ')');
  }
  
  @Override
  public Rect clone()
  {
    return new Rect(x, y, w, h);
  }

  // query
  /**
   * Check whether the Rectangle contains a given point.
   *
   * @param px the horizontal position of the point.
   * @param py the vertical position of the point.
   * @return true if the point is inside the Rectangle, false otherwise.
   */
  public boolean contains(float px, float py)
  {
    return (px >= x && px <= x + w && py >= y && py <= y + h);
  }

  /**
   * Check whether the Rectangle contains a given point (vector format).
   *
   * @param position the vector position of the point.
   * @return true if the point is inside the Rectangle, false otherwise.
   */
  public boolean contains(V2 position)
  {
    return contains(position.x(), position.y());
  }

  /**
   * Check whether this Rectangle is completely inside another.
   *
   * @param other the Rectangle which we may or may not be inside of.
   * @return true if this Rectangle is entirely contained with the other.
   * @see <a href="Rect#collides(wjd.math.Rect)">collides(Rect other)</a>
   */
  public boolean inside(Rect other)
  {
    return (other.contains(x, y)
      && other.contains(x + w, y)
      && other.contains(x, y + h)
      && other.contains(x + w, y + h));
  }

  /**
   * Check whether any part of this Rectangle is colliding with another,
   * including sides intersecting but also one being completely inside the
   * other.
   *
   * @param other the Rectangle to check collisions with.
   * @return true if any sort of collision has occurred, false otherwise.
   * @see <a href="Rect#inside(wjd.math.Rect)">inside(Rect other)</a>
   */
  public boolean collides(Rect other)
  {
    return (V2.dot(new V2(pos(), other.pos().add(other.size())),
      new V2(other.pos(), pos().add(size()))) > 0);
  }

  /**
   * Check whether any part of the specified segment enters the Rectangle.
   *
   * @param start the start position of the segment.
   * @param end the end position of the segment.
   * @return true is any part of the segment enters this Rectangle.
   */
  public boolean collides(V2 start, V2 end)
  {
    // discard useless states
    if (Math.min(start.x(), end.x()) > x + w
      || Math.max(start.x(), end.x()) < x)
      return false;
    if (Math.min(start.y(), end.y()) > y + h
      || Math.max(start.y(), end.y()) < y)
      return false;

    // otherwise it's all good!
    return true;
  }

  /**
   * Check how much bigger (or smaller) this Rectangle is than another one.
   *
   * @param other the Rectangle to check overlap with.
   * @return a vector the two field of which represent the horizontal and
   * vertical overlap: positive if this Rectangle is bigger than the other,
   * negative if not.
   */
  public V2 overlap(Rect other)
  {
    return new V2(w - other.w, h - other.h);
  }

  // base mutators
  /**
   * Set the horizontal position of the Rectangle.
   *
   * @param x the new value for the horizontal offset.
   * @return this, so that multiple operations can be queued.
   */
  public Rect x(float x)
  {
    this.x = x;
    return this;
  }

  /**
   * Set the vertical position of the Rectangle.
   *
   * @param y the new value for the vertical offset.
   * @return this, so that multiple operations can be queued.
   */
  public Rect y(float y)
  {
    this.y = y;
    return this;
  }

  /**
   * Set the vertical and horizontal positions of the Rectangle.
   *
   * @param x the new value for the horizontal offset.
   * @param y the new value for the vertical offset.
   * @return this, so that multiple operations can be queued.
   */
  public Rect xy(float x, float y)
  {
    this.x = x;
    this.y = y;
    return this;
  }

  /**
   * Set the width of the Rectangle.
   *
   * @param w the new width value.
   * @return this, so that multiple operations can be queued.
   */
  public Rect w(float w)
  {
    this.w = w;
    return this;
  }

  /**
   * Set the height of the Rectangle.
   *
   * @param h the new height value.
   * @return this, so that multiple operations can be queued.
   */
  public Rect h(float h)
  {
    this.h = h;
    return this;
  }

  /**
   * Set the width and the height of the Rectangle.
   *
   * @param w the new width value.
   * @param h the new height value.
   * @return this, so that multiple operations can be queued.
   */
  public Rect wh(float w, float h)
  {
    this.w = w;
    this.h = h;
    return this;
  }

  /**
   * Set the vertical and horizontal positions and the width and the height of
   * the Rectangle.
   *
   * @param x the new value for the horizontal offset.
   * @param y the new value for the vertical offset.
   * @param w the new width value.
   * @param h the new height value.
   * @return this, so that multiple operations can be queued.
   */
  public Rect reset(float x, float y, float w, float h)
  {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    return this;
  }

  /**
   * Reset the width-to-height ratio of the Rectangle.
   *
   * @param ratio the new (w / h) ratio
   * @return this, so that multiple operations can be queued.
   */
  public Rect ratio(float ratio)
  {
    return (ratio > ratio()) ? h(w / ratio) : w(h * ratio);
  }
  // arithmetic mutators

  /**
   * Shift the Rectangle left or right.
   *
   * @param shift_x amount to be added to the horizontal offset
   * @return this, so that multiple operations can be queued.
   */
  public Rect xadd(float shift_x)
  {
    return x(x + shift_x);
  }

  /**
   * Shift the Rectangle up or down.
   *
   * @param shift_y amount to be added to the vertical offset
   * @return this, so that multiple operations can be queued.
   */
  public Rect yadd(float shift_y)
  {
    return y(y + shift_y);
  }

  /**
   * Shift the Rectangle both vertically and horizontally.
   *
   * @param shift_x amount to be added to the horizontal offset
   * @param shift_y amount to be added to the vertical offset
   * @return this, so that multiple operations can be queued.
   */
  public Rect shift(float shift_x, float shift_y)
  {
    return xy(x + shift_x, y + shift_y);
  }

  /**
   * Multiply the size of the Rectangle by a scalar.
   *
   * @param multiplier a real value by which the height and width of the
   * Rectangle will be multiplied
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#stretch(float)">stretch</a>
   */
  public Rect stretch(float multiplier)
  {
    return wh(w * multiplier, h * multiplier);
  }

  // element-wise arithmetic mutators
  /**
   * Reset the position of the Rectangle.
   *
   * @param position the new position where the top-left corner of the Rectangle
   * will be placed.
   * @return this, so that multiple operations can be queued.
   */
  public Rect pos(V2 position)
  {
    return xy(position.x(), position.y());
  }

  /**
   * Reset the size of the Rectangle.
   *
   * @param size the new size of the Rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect size(V2 size)
  {
    return wh(size.x(), size.y());
  }

  /**
   * Reset the size and the position of the Rectangle.
   *
   * @param position the new position where the top-left corner of the Rectangle
   * will be placed.
   * @param size the new size of the Rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect reset(V2 position, V2 size)
  {
    return reset(position.x(), position.y(), size.x(), size.y());
  }

  /**
   * Move the Rectangle based on a vector where the components represent the
   * horizontal and vertical translations respectively.
   *
   * @param shift the (x, y) vector used element-wise to perform the shift.
   * @return this, so that multiple operations can be queued.
   */
  public Rect shift(V2 shift)
  {
    return xy(x + shift.x(), y + shift.y());
  }

  /**
   * Move the Rectangle in the opposite direction of that of a vector, where the
   * components represent the horizontal and vertical translations respectively.
   *
   * @param unshift the (x, y) vector used element-wise to perform the shift.
   * @return this, so that multiple operations can be queued.
   */
  public Rect unshift(V2 unshift)
  {
    return xy(x - unshift.x(), y - unshift.y());
  }

  /**
   * Multiply the size of the Rectangle element-wise by a vector
   *
   * @param multiplier a pair of real values by which the height and width of
   * the Rectangle will be multiplied respectively.
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#stretch(float)">stretch</a>
   */
  public Rect stretch(V2 multiplier)
  {
    return wh(w * multiplier.x(), h * multiplier.y());
  }
  // geometric mutators

  /**
   * Reset the position of the centre of the Rectangle.
   *
   * @param position the new position of the centre of the Rectangle.
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#pos(V2)">pos</a>
   */
  public Rect centreOn(V2 position)
  {
    this.
    x = position.x() - w * 0.5f;
    y = position.y() - h * 0.5f;
    return this;
  }

  /**
   * Centre within another Rectangle: this is especially useful for building
   * interfaces where boxes contains other boxes.
   *
   * @param container the Rectangle within which this one will be centred.
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#centreOn(Rect)">centreOn</a>
   */
  public Rect centreWithin(Rect container)
  {
    if (w > container.w && h > container.h)
      return this;
    else if (w > container.w)
      wh(container.w, h * container.w / w);
    else if (h > container.h)
      wh(w * container.h / h, container.h);

    return xy(container.x + container.w * 0.5f - w * 0.5f,
      container.y + container.h * 0.5f - h * 0.5f);
  }

  /**
   * Multiply the size of the Rectangle by a scalar, moving the Rectangle at the
   * same time so that it stays centred on its original centre.
   *
   * @param multiplier a real value by which the height and width of the
   * Rectangle will be multiplied
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#stretch(float)">stretch</a>
   */
  public Rect scale(float multiplier)
  {
    return shift(size().scale(1.0f - multiplier).scale(0.5f)).
      stretch(multiplier);
  }
}
