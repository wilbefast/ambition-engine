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

package wjd.amb.resources;

import wjd.math.Rect;

/**
 *
 * @author wdyce
 * @since Jan 16, 2013
 */
public class Tileset extends Graphic
{
  /* ATTRIBUTES */
  private int n_across, n_high;

  /* METHODS */
  
  // constructors
  public Tileset(ITexture _texture, Rect _frame, int _n_across, int _n_high)
  {
    super(_texture, _frame);
    this.n_across = _n_across;
    this.n_high = _n_high;
  }
  
  // query
  public void getFrame(int n, Rect result)
  {
    
    //convert from 'n'th image to 'y'th line and 'x'th collumn
    int row = n / n_across;
    int col = n % n_across;

    result.reset(col * frame.w, row * frame.h, frame.w, frame.h);
  }
}
