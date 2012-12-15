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
package wjd.amb.rts;

import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 22, 2012
 */
abstract class AHeuristic
{
  /* INTERFACE */
  public abstract int estimate(V2 start, V2 end);

  /* IMPLEMENTATIONS */
    
  public static final AHeuristic NONE = new AHeuristic()
  {
    @Override
    public int estimate(V2 start, V2 end)
    {
      return 0;
    }
  };
  
  public static final AHeuristic EUCLIEAN = new AHeuristic()
  {
    @Override
    public int estimate(V2 start, V2 end)
    {
      return 
        (int)new V2(start, end).norm();
    }
  };
  
  public static final AHeuristic MANHATTAN = new AHeuristic()
  {
    @Override
    public int estimate(V2 start, V2 end)
    {
      V2 s = start, e = end;
      return (int)(Math.abs(s.x - e.x) + Math.abs(s.y - e.y));
    }
  };
}
