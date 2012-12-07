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
 * Static mathematical functions missing form Java's Math class.
 *
 * @author wdyce
 * @since Dec 6, 2012
 */
public abstract class M 
{
  /* FUNCTIONS */
  
  /**
   * Calculate the square of a value.
   * 
   * @param x the value to square.
   * @return x squared.
   */
  public static double sqr(double x)
  {
    return x*x;
  }
  
  /**
   * Calculate the integral square-root of a value, that is the highest integer
   * which, squared, is smaller than the value in question.
   * 
   * @param x the value to get the integral square-root of.
   * @return the integral square-root of x.
   */
  public static int isqrt(double x)
  {
    if(x < 0)
      return 0;
    
    int i = 0;
    while(i*i <= x) i++;
    return (i-1);
  }
}
