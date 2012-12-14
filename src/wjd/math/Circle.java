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
 *
 * @author wdyce
 * @since Dec 14, 2012
 */
public class Circle 
{
  /* ATTRIBUTES */
  public float radius;
  public final V2 centre = new V2();
  private final Rect bounding_box = new Rect();
  
  /* METHODS */

  // constructors
  
  public Circle()
  {
    centre.x = centre.y = radius = 0.0f;
  }
  
  public Circle(V2 centre_, float radius_)
  {
    this.centre.reset(centre_);
    this.radius = radius_;
  }
  
  public Circle(float radius_)
  {
    this.radius = radius_;
  }
  
  public Circle(V2 centre_)
  {
    this.centre.reset(centre_);
  }

  // accessors
  
  /**
   * Generate a random point within the radius.
   * 
   * @param result where to write the resulting point.
   * @return this, so multiple operations can be queued.
   */
  public Circle randomPoint(V2 result)
  {
    double r = Math.random() * radius, t = Math.random() * 2 * Math.PI;
    result.xy((float)(Math.cos(t)*r), (float)(Math.sin(t)*r)).add(centre);
    return this;
  }
  
  public boolean collides(Circle other)
  {
    return (centre.distance2(other.centre) <= M.sqr(other.radius+radius));
  }
  
  public boolean collides(Rect query_area)
  {
    getBoundingBox(bounding_box);
    return bounding_box.collides(query_area);
  }
  
  public boolean inside(Rect query_area)
  {
    getBoundingBox(bounding_box);
    return bounding_box.inside(query_area);
  }
  
  public Circle getBoundingBox(Rect result)
  {
    result.reset(centre.x-radius, centre.y-radius, 2*radius, 2*radius);
    return this;
  }

  // mutators
  
  public Circle setCentre(V2 centre_)
  {
    centre.reset(centre_);
    return this;
  }
  
  public Circle setCentre(float x, float y)
  {
    centre.x = x;
    centre.y = y;
    return this;
  }
  
  
}
