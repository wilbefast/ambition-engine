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

/**
 * Basic Colour class.
 *
 * @author william
 * @since Sep 29, 2012
 */
public class Colour
{
  /* CONSTANTS */
  /**
   * Primitive Colour constant for quick access.
   */
  public static final Colour BLACK = new Colour(0.0f, 0.0f, 0.0f),
    WHITE = new Colour(1.0f, 1.0f, 1.0f),
    RED = new Colour(1.0f, 0.0f, 0.0f),
    GREEN = new Colour(0.0f, 1.0f, 0.0f),
    BLUE = new Colour(0.0f, 0.0f, 1.0f),
    TEAL = new Colour(0.0f, 1.0f, 1.0f),
    VIOLET = new Colour(1.0f, 0.0f, 1.0f),
    YELLOW = new Colour(1.0f, 1.0f, 0.0f);

  /* ATTRIBUTES */
  /**
   * The green channel, between 0.0f and 1.0f.
   */
  public float r;
  /**
   * The red channel, between 0.0f and 1.0f.
   */
  public float g;
  /**
   * The blue channel, between 0.0f and 1.0f.
   */
  public float b;
  /**
   * The alpha (transparency) channel, between 0.0f and 1.0f.
   */
  public float a;

  /* METHODS */
  // constructors
  /**
   * Create a Colour from a set of real values (between 0.0f and 1.0f), where 0
   * is empty (black) and 1 is full (white) for each channel, setting the alpha
   * channel to a default value of 1.0f (totally opaque).
   *
   * @param r red channel, between 0.0f and 1.0f.
   * @param g green channel, between 0.0f and 1.0f.
   * @param b blue channel, between 0.0f and 1.0f.
   */
  public Colour(float r, float g, float b)
  {
    this(r, g, b, 1.0f);
  }

  /**
   * Create a Colour from a set of real values (between 0.0f and 1.0f), where 0
   * is empty (black) and 1 is full (white) for each channel.
   *
   * @param r red channel, between 0.0f and 1.0f.
   * @param g green channel, between 0.0f and 1.0f.
   * @param b blue channel, between 0.0f and 1.0f.
   * @param a alpha channel (transparency), between 0.0f and 1.0f.
   */
  public Colour(float r, float g, float b, float a)
  {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
  }

  /**
   * Create a Colour from a set of bytes (values between 0 and 255), setting the
   * alpha channel to a default value of 255 (totally opaque).
   *
   * @param r red channel, between 0 and 255.
   * @param g green channel, between 0 and 255.
   * @param b blue channel, between 0 and 255.
   */
  public Colour(int r, int g, int b)
  {
    this(r, g, b, 255);
  }

  /**
   * Create a colour from a set of bytes (values between 0 and 255).
   *
   * @param r red channel, between 0 and 255.
   * @param g green channel, between 0 and 255.
   * @param b blue channel, between 0 and 255.
   * @param a alpha channel (transparency), between 0 and 255.
   */
  public Colour(int r, int g, int b, int a)
  {
    this.r = ((r > 255) ? 255 : r) / 255.0f;
    this.g = ((g > 255) ? 255 : g) / 255.0f;
    this.b = ((b > 255) ? 255 : b) / 255.0f;
    this.a = ((a > 255) ? 255 : a) / 255.0f;
  }

  // mutators
  /**
   * Add another Colour to this Colour (works like mixing light), modifying red,
   * green and blue (but not alpha) channels.
   *
   * @param other the Colour to be added.
   * @return this, the resulting Colour, so more operations can be queued on it.
   */
  public Colour add(Colour other)
  {
    r += other.r;
    g += other.g;
    b += other.b;
    return this;
  }

  /**
   * Add another Colour to this Colour (works like mixing paint), modifying red,
   * green and blue (but not alpha) channels.
   *
   * @param other the Colour to be subtracted.
   * @return this, the resulting Colour, so more operations can be queued on it.
   */
  public Colour sub(Colour other)
  {
    r -= other.r;
    g -= other.g;
    b -= other.b;
    return this;
  }
  
  /**
   * Mix two Colours together to get a third.
   * 
   * @param other the Colour to be mixed in.
   * @return this, the resulting Colour, so more operations can be queued on it.
   */
  public Colour avg(Colour other)
  {
    r = (r+other.r)*0.5f;
    g = (g+other.g)*0.5f;
    b = (b+other.b)*0.5f;
    return this;
  }
  
  public Colour mult(float multiplier)
  {
    r *= multiplier;
    g *= multiplier;
    b *= multiplier;
    return this;
  }
  
  // accessors
  
  @Override
  public Colour clone()
  {
    return new Colour(r, g, b, a);
  }

  /* OVERRIDES -- OBJECTS */
  
  @Override
  public String toString()
  {
    return "rgba(" + r + "," + g + "," + b + "," + a + ")"; 
  }
}
