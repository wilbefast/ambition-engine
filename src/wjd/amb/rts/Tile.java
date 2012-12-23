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

import wjd.amb.control.IDynamic;
import wjd.amb.view.IVisible;
import wjd.math.Rect;
import wjd.math.V2;

/**
 *
 * @author wdyce
 * @since Nov 1, 2012
 */
public abstract class Tile implements IVisible, IDynamic
{
  /* CONSTANTS */
  /*public static final V2 SIZE = new V2(32, 32);
  public static final V2 HSIZE = SIZE.clone().scale(0.5f);
  public static final V2 ISIZE = SIZE.clone().inv();*/

  /* ATTRIBUTES */
  public final TileGrid grid;
  public final V2 grid_position, pixel_position;
  protected final Rect pixel_area;

  /* METHODS */
  
  // constructors
  public Tile(int row, int col, V2 size, TileGrid grid)
  {
    grid_position = new V2(col, row);
    pixel_position = grid_position.clone().scale(size);
    pixel_area = new Rect(pixel_position, size);
    this.grid = grid;
  } 
  
  /* INTERFACE */
  
  public abstract boolean isPathable();
}
