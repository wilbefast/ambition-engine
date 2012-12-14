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
package wjd.util;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author wdyce
 * @since Dec 13, 2012
 */
public class ListPairIterator<T> implements Iterator<Pair<T>>
{
  /* ATTRIBUTES */
  private int i = 0, j = 1;
  private T[] elements;
  
  /* METHODS */

  // constructors
  public ListPairIterator(List<T> list)
  {
    elements = (T[])list.toArray();
  }

  // accessors

  // mutators
  
  @Override
  public boolean hasNext()
  {
    return (j < elements.length);
  }

  @Override
  public Pair<T> next()
  {
    Pair<T> result = new Pair(elements[i], elements[j++]);
    if(j >= elements.length)
    {
      i++;
      j = i+1;
    }
    return result;
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException("Remove not supported.");
  }
}
