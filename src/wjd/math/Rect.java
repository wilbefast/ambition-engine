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

import java.io.Serializable;

/**
 * An axis-aligned 2D rectangle, used for view testing and basic collision 
 * checking.
 *
 * @author wdyce
 * @since 24 Aug, 2012
 */
public class Rect implements Serializable
{
  /* ATTRIBUTES */
  /**
   * The horizontal offset.
   */
  public float x;
  /**
   * The vertical offset.
   */
  public float y;
  /**
   * The width.
   */
  public float w;
  /**
   * The height.
   */
  public float h;

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
   * Create a rectangle of the specified size at the origin.
   * 
   * @param w the width.
   * @param h the height.
   */
  public Rect(float w, float h)
  {
    this.x = this.y = 0;
    this.w = w;
    this.h = h;
  }

  /**
   * Create a rectangle of the specified size at the origin.
   *
   * @param size the size vector: (w, h).
   */
  public Rect(V2 size)
  {
    x = y = 0.0f;
    w = size.x;
    h = size.y;
  }

  /**
   * Create a rectangle with the specified vector offset (position) and size.
   *
   * @param position the position vector: (x, y).
   * @param size the size vector: (w, h).
   */
  public Rect(V2 position, V2 size)
  {
    x = position.x;
    y = position.y;
    w = size.x;
    h = size.y;
  }

  // accessors
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
   * 
   * @return the position of the bottom-right corner of the Rectangle.
   */
  public V2 endpos()
  {
    return new V2(x + w, y + h);
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
  
  public V2 getCentre()
  {
    return new V2(x+w/2, y+h/2);
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
    return contains(position.x, position.y);
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
    float v1x = other.endx() - x, v2x = endx() - other.x;
    if(v1x < 0 && v2x >= 0 || v1x > 0 && v2x <= 0)
      return false;
    
    float v1y = other.endy() - y, v2y = endy() - other.y;
    if(v1y < 0 && v2y >= 0 || v1y > 0 && v2y <= 0)
      return false;   
    
    return true;
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
    if (Math.min(start.x, end.x) > x + w
        || Math.max(start.x, end.x) < x)
      return false;
    if (Math.min(start.y, end.y) > y + h
        || Math.max(start.y, end.y) < y)
      return false;

    // otherwise it's all good!
    return true;
  }
  
  /**
   * Find the intersection between this Rectangle and another one.
   * 
   * @param other the other Rectangle to find an intersection with.
   * @return the intersection Rectangle or (0,0,0,0) if there is no 
   * intersection.
   */
  public Rect getIntersection(Rect other)
  {
    // Calculate the boundaries of the intersection
    float left = Math.max(x, other.x);
    float top = Math.max(y, other.y);
    float right = Math.min(x + w,  other.x + other.w);
    float bottom = Math.min(y + h,  other.y + other.h);

    // If the intersection is invalid (negative lengths) return false
    if((left > right ) || (top > bottom))
      return null;
    else //non-negative lengths
      return new Rect(left, top, right - left, bottom - top);
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
   * Side the horizontal extent of the rectangle.
   *
   * @param endx the new rightmost abscissa of the rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect endx(float endx)
  {
    w = endx - x;
    return this;
  }

  /**
   * Set the vertical extent of the rectangle.
   *
   * @param endy the new bottommost ordinate of the rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect endy(float endy)
  {
    h = y - endy;
    return this;
  }

  /**
   * Set the extent of the rectangle, horizontal and vertical.
   *
   * @param endx the new rightmost abscissa of the rectangle.
   * @param endy the new bottommost ordinate of the rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect endxy(float endx, float endy)
  {
    w = endx - x;
    h = endy - y;
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
    if(ratio > ratio)
      h = w/ratio;
    else
      w = h*ratio;
    return this;
  }
  // arithmetic mutators

  /**
   * Shift the Rectangle both vertically and horizontally.
   *
   * @param shift_x amount to be added to the horizontal offset
   * @param shift_y amount to be added to the vertical offset
   * @return this, so that multiple operations can be queued.
   */
  public Rect shift(float shift_x, float shift_y)
  {
    x += shift_x;
    y += shift_y;
    return this;
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
    w *= multiplier;
    h *= multiplier;
    return this;
  }
  
  public Rect stretch(int dw, int dh)
  {
    w += dw;
    h += dh;
    return this;
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
    x = position.x;
    y = position.y;
    return this;
  }
  
  public Rect endpos(V2 endposition)
  {
    return endxy(endposition.x, endposition.y);
  }

  /**
   * Reset the size of the Rectangle.
   *
   * @param size the new size of the Rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect size(V2 size)
  {
    return wh(size.x, size.y);
  }
  
  /**
   * Reset the size of the Rectangle in such a way that it keeps the same centre
   * as before.
   * 
   * @param size the new size of the Rectangle.
   * @return this, so that multiple operations can be queued.
   */
  public Rect centreSize(V2 size)
  {
    float centre_x = x + w*0.5f, centre_y = y + h*0.5f;
    return size(size).centrexy(centre_x, centre_y);
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
    return reset(position.x, position.y, size.x, size.y);
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
    return xy(x + shift.x, y + shift.y);
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
    return xy(x - unshift.x, y - unshift.y);
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
    return wh(w * multiplier.x, h * multiplier.y);
  }
  
  /**
   * Divide the size of the Rectangle element-wise by a vector
   *
   * @param divisor a pair of real values by which the height and width of
   * the Rectangle will be divided respectively.
   * @return this, so that multiple operations can be queued.
   */
  public Rect shrink(V2 divisor)
  {
    return wh(w / divisor.x, h / divisor.y);
  }
  
  // geometric mutators

  /**
   * Reset the position of the centre of the Rectangle based on a vector.
   *
   * @param position the new position of the centre of the Rectangle.
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#pos(V2)">pos</a>
   */
  public Rect centrePos(V2 position)
  {
    x = position.x - w * 0.5f;
    y = position.y - h * 0.5f;
    return this;
  }
  
   /**
   * Reset the position of the centre of the Rectangle based on a pair of 
   * coordinates.
   *
   * @param cx the desired horizontal centre.
   * @param cy the desired vertical centre.
   * @return this, so that multiple operations can be queued.
   * @see <a href="Rect#xy(float, float)">xy</a>
   */
  public Rect centrexy(float cx, float cy)
  {
    x = cx - w * 0.5f;
    y = cy - h * 0.5f;
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

  public Rect reset(Rect r)
  {
    x = r.x;
    y = r.y;
    w = r.w;
    h = r.h;
    return this;
  }
  
  /**
   * Multiply the abscissa and width by the first element of the vector, the
   * ordinate and height by the second.
   * 
   * @param multiplier the vector pair to element-wise multiply by.
   * @return this, so that multiple operations can be queued.
   */
  public Rect mult(V2 multiplier)
  {
    x *= multiplier.x;
    y *= multiplier.y;
    w *= multiplier.x;
    h *= multiplier.y;
    return this;
  }
  
  /**
   * Divide the abscissa and width by the first element of the vector, the
   * ordinate and height by the second.
   * 
   * @param divisor the vector pair to element-wise divide by.
   * @return this, so that multiple operations can be queued.
   */
  public Rect div(V2 divisor)
  {
    x /= divisor.x;
    y /= divisor.y;
    w /= divisor.x;
    h /= divisor.y;
    return this;
  }
  
  /**
   * Floor the size and position values of the Rectangle to the integer below.
   * 
   * @return this, so that multiple operations can be queued.
   */
  public Rect floor()
  {
    x = (int)x;
    y = (int)y;
    w = (int)w;
    h = (int)h;
    return this;
  }
  
  /**
   * Ceil the size and position values of the Rectangle to the integer above.
   * 
   * @return this, so that multiple operations can be queued.
   */
  public Rect ceil()
  {
    x = (int)x + 1;
    y = (int)y + 1;
    w = (int)w + 1;
    h = (int)h + 1;
    return this;
  }
  
  /**
   * If the Rectangle has been turned inside-out, turn it the right way around 
   * again: the area covered should be the same, but the origin is placed in the
   * top-left not the bottom-right.
   * 
   * @return this, so that multiple operations can be queued.
   */
  public Rect makePositive()
  {
    if(w < 0)
    {
      w *= -1;
      x -= w;
    }
    if(h < 0)
    {
      h *= -1;
      y -= h;
    }
    return this;
  }
  
  /**
   * Generate a random point within this area.
   * 
   * @param destination the vector object to write the result to.
   * @return this, so that multiple operations can be queued.
   */
  public Rect randomPoint(V2 destination)
  {
    destination.xy(x + (float)Math.random()*w, y + (float)Math.random()*h);
    return this;
  }
  
  /**
   * Swap the width and height.
   * 
   * @return this, so that multiple operations can be queued.
   */
  public Rect turn90()
  {
    float old_w = w, 
          old_cx = x + 0.5f*w, 
          old_cy = y + 0.5f*h;
    w = h;
    h = old_w;
    return centrexy(old_cx, old_cy);
  }
}
