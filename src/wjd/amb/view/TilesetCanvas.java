/*
 Copyright (C) 2013 William James Dyce

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

import wjd.amb.resources.Tileset;
import wjd.math.Rect;

/**
 *
 * @author wdyce
 * @since Jan 16, 2013
 */
public class TilesetCanvas extends GraphicCanvas
{
  /* ATTRIBUTES -- inherited */
  // private Graphic graphic;
  // private iRect dest;

  /* ATTRIBUTES */
  public int tile_i = 0;

  /* METHODS */
  // creation
  public TilesetCanvas(Tileset init_tset, Rect init_dest)
  {
    super(init_tset, init_dest);
  }
  
  // mutators
  
  /* SUBROUTINES */
  @Override
  protected void getSubrect(Rect result)
  {
    // offset the frame based on current tile subimage
    ((Tileset) graphic).getFrame(tile_i, result);
  }
}
