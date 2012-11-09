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
 * A useful Java vector class for games and graphic applications: the norm is
 * cached and recalculated only when needed, and there are a number of useful
 * methods implemented.
 *
 * @author wdyce
 * @since 24 Aug, 2012
 */
public class V2 implements Serializable
{
  /* CONSTANTS */
  /**
   * Vector origin, (0,0).
   */
  public static final V2 ORIGIN = new V2(0.0f, 0.0f);
  
  /* GLOBAL */
  /**
   * A table of vectors for use in intermediary calculations: not safe to use 
   * for functions that may be executed in parallel with others.
   */
  public static V2 temp[] = { new V2(), new V2(), new V2(), new V2() };

  /* FUNCTIONS */
  /**
   * The area of a parallelogram is the absolute value of the determinant of the
   * matrix formed by the vectors representing the parallelogram's sides.
   *
   * @see <a href="http://en.wikipedia.org/wiki/Determinant">Wikipedia</a>
   * @param a the first vector.
   * @param b the other vector.
   * @return the determinant of the two vectors.
   */
  public static float det(V2 a, V2 b)
  {
    return a.x * b.y - b.y * a.x;
  }

  /**
   * The dot product, also known as scalar or inner product, is used to
   * determine the length of the project of one vector onto another and hence to
   * find out, for instance, whether two vectors are perpendicular.
   *
   * @param a the first vector.
   * @param b the other vector.
   * @return (a.x*b.x + a.y*b.y);
   */
  public static float dot(V2 a, V2 b)
  {
    return a.x * b.x + a.y * b.y;
  }

  /**
   * Calculate the linear interpolation of two vector positions based on a given
   * real value between 0 and 1.
   *
   * @param a the first vector.
   * @param b the other vector.
   * @param frac real value representing the percent distance between a (0) and
   * b (1).
   * @return <li>= a if frac is less than or equal to 0. <li>= b if frac is
   * greater than or equal to 1. <li>= a + (b-a)*frac otherwise.
   */
  public static V2 inter(V2 a, V2 b, float frac)
  {
    return (frac < 0)
      ? a : ((frac > 1)
      ? b : (new V2(a, b).scale(frac).add(b)));
  }

  /**
   * Check if two vectors and collinear, in other words parallel.
   *
   * @param a the first vector.
   * @param b the other vector.
   * @return true if the two vectors are collinear, false if not.
   */
  public static boolean coline(V2 a, V2 b)
  {
    return dot(a, b) == a.norm() * b.norm();
  }
  /* ATTRIBUTES */
  /**
   * abscissa value: horizontal component.
   */
  public float x;
  /**
   * ordinate value: vertical component.
   */
  public float y;
 
  /* METHODS */
  // constructors
  /**
   * The null vector (0, 0)
   */
  public V2()
  {
    x = y = 0.0f;
  }

  /**
   * Vector created with specified abscissa (x) and ordinate (y)
   *
   * @param x the abscissa of the vector, its horizontal component.
   * @param y the ordinate of the vector, its vertical component.
   */
  public V2(float x, float y)
  {
    this.x = x;
    this.y = y;
  }

  /**
   * Vector created from a pair of point, id est the vector direction from the
   * first point to the second.
   *
   * @param source the starting point of the new vector.
   * @param destination the end point of the new vector.
   */
  public V2(V2 source, V2 destination)
  {
    x = destination.x - source.x;
    y = destination.y - source.y;
  }

  // accessors

  /**
   * Check if this is a null vector.
   *
   * @return true if this is a null vector, false otherwise
   */
  public boolean zero()
  {
    return (x == 0 && y == 0);
  }

  /**
   * Check the square norm of this vector: this is faster to calculate than the
   * norm itself, so this methods should be used where possible (ie: the norm is
   * less than a value v if the square norm is less than v squared).
   *
   * @return the square norm of the vector.
   * @see <a href="V2#norm()>norm2</a>
   */
  public float norm2()
  {
    // recalculate norm2 only if nessecary
    return (x*x + y*y);
  }

  /**
   * Return the norm (magnitude, length) of the vector: it is preferable to use
   * the square norm where possible (ie: the norm is less than a value v if the
   * square norm is less than v squared).
   *
   * @return the norm of the vector.
   * @see <a href="V2#norm2()>norm2</a>
   */
  public float norm()
  {
    return (float)Math.sqrt(x*x + y*y);
  }

  /**
   * Get a vector containing the sign of each component.
   *
   * @return a vector where the components correspond to the signs of this
   * vector's components.
   */
  public V2 sign()
  {
    return new V2(Math.signum(x), Math.signum(y));
  }

  /**
   * Convert to String, for debugging.
   *
   * @return a String formatted as follows: (x, y)
   */
  @Override
  public String toString()
  {
    return ("(" + x + ',' + y + ')');
  }

  @Override
  public V2 clone()
  {
    return new V2(x, y);
  }

  // base mutators

  /**
   * Reset the horizontal and vertical components of the vector.
   *
   * @param x the new value for the abscissa.
   * @param y the new value for the ordinate.
   * @return this, so that multiple operations can be queued.
   */
  public V2 xy(float x, float y)
  {
    this.x = x;
    this.y = y;
    return this;
  }

  // arithmetic mutators

  /**
   * Add a real value to the abscissa (x) and the ordinate (y).
   *
   * @param dx amount to be added to the horizontal component
   * @param dy amount to be added to the vertical component
   * @return this, so that multiple operations can be queued.
   */
  public V2 add(float dx, float dy)
  {
    return xy(x + dx, y + dy);
  }

  /**
   * Multiply the horizontal and vertical components of the vector by a real.
   *
   * @param multiplier the real value to multiply the vector by.
   * @return this, so that multiple operations can be queued.
   */
  public V2 scale(float multiplier)
  {
    return xy(x * multiplier, y * multiplier);
  }

  /**
   * Add 1 to the horizontal and vertical components of the vector.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 inc()
  {
    return xy(x + 1, y + 1);
  }

  /**
   * Subtract 1 from the horizontal and vertical components of the vector.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 dinc()
  {
    return xy(x - 1, y - 1);
  }
  
  /**
   * Invert the vector, setting its components to 1/x and 1/y.
   * 
   * @return this, so that multiple operations can be queued.
   */
  public V2 inv()
  {
    return xy (1 / x, 1 / y);
  }

  /**
   * Set the horizontal and vertical components of the vector to their
   * respective absolute values.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 abs()
  {
    return xy((float) Math.abs(x), (float) Math.abs(y));
  }

  /**
   * Round down the horizontal and vertical components of the vector.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 floor()
  {
    return xy((float) Math.floor(x), (float) Math.floor(y));
  }

  /**
   * Round up the horizontal and vertical components of the vector.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 ceil()
  {
    return xy((float) Math.ceil(x), (float) Math.ceil(y));
  }
  // element-wise arithmetic mutators

  /**
   * Add a vector to this one.
   *
   * @param amount the vector to be added.
   * @return this, so that multiple operations can be queued.
   */
  public V2 add(V2 amount)
  {
    return xy(x + amount.x, y + amount.y);
  }

  /**
   * Subtract a vector from this one.
   *
   * @param amount the vector to be added.
   * @return this, so that multiple operations can be queued.
   */
  public V2 sub(V2 amount)
  {
    return xy(x - amount.x, y - amount.y);
  }
  
  /**
   * Copy the contents other another vector into this one.
   * 
   * @param new_value vector to copy new values of abscissa and ordinate from.
   * @return this, so that multiple operations can be queued. 
   */
  public V2 reset(V2 new_value)
  {
    return xy(new_value.x, new_value.y);
  }

  /**
   * Multiply a vector element-wise by another.
   *
   * @param multiplier a vector containing the values that the abscissa (x) and
   * ordinate (y) of this vector will be multiplied by.
   * @return this, so that multiple operations can be queued.
   */
  public V2 scale(V2 multiplier)
  {
    return xy(x * multiplier.x, y * multiplier.y);
  }
  
  /**
   * Divide a vector element-wise by another.
   * 
   * @param divisor a vector containing the values that the abscissa (x) and
   * ordinate (y) of this vector will be divided by.
   * @return this, so that multiple operations can be queued. 
   */
  public V2 shrink(V2 divisor)
  {
    return xy(x / divisor.x, y / divisor.y);
  }
  
  // geometric mutators

  /**
   * Rotate the vector 90 degrees to the left (a right-angle): this is a very
   * cheap operation, while custom rotations are expensive.
   *
   * @return this, so that multiple operations can be queued.
   * @see <a href="V2#addAngle(float)">addAngle</a>
   */
  public V2 left()
  {
    return xy(y, -x);
  }

  /**
   * Rotate the vector 90 degrees to the right (a right-angle): this is a very
   * cheap operation, while custom rotations are expensive.
   *
   * @return this, so that multiple operations can be queued.
   * @see <a href="V2#addAngle(float)">addAngle</a>
   */
  public V2 right()
  {
    return xy(-y, x);
  }

  /**
   * Set the norm, magnitude or length of the vector.
   *
   * @param amount the new norm, magnitude or length.
   * @return this, so that multiple operations can be queued.
   */
  public V2 norm(float amount)
  {
    return xy(x / norm() * amount, y / norm() * amount);
  }

  /**
   * Add a real value to the norm of the vector to make it longer or shorter.
   *
   * @param amount the amount to be added to the norm.
   * @return this, so that multiple operations can be queued.
   */
  public V2 addNorm(float amount)
  {
    return norm(norm() + amount);
  }

  /**
   * Set the norm of the vector to 1.
   *
   * @return this, so that multiple operations can be queued.
   */
  public V2 normalise()
  {
    return (norm(1 / norm()));
  }

  /**
   * Rotate the vector clockwise around the origin: this requires trigonometric
   * functions so is expensive.
   *
   * @param angle the angle, in radians, to rotate the vector.
   * @return this, so that multiple operations can be queued.
   * @see <a href="V2#left()">left</a> or <a href="V2#right()">right</a>
   */
  public V2 addAngle(float angle)
  {
    double cos = Math.cos(angle), sin = Math.sin(angle);
    return xy((float) (x * cos - y * sin), (float) (x * sin + y * cos));
  }
}
