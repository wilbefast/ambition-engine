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

/**
 *
 * @author wdyce
 * @since Dec 13, 2012
 */
public class ArrayIterator<T> implements Iterator<T>
{
  /* ATTRIBUTES */
  private final T[] array;
  private int i = 0;
  
  /* METHODS */
  public ArrayIterator(T[] array_)
  {
    this.array = array_;
  }

  // constructors

  // accessors

  // mutators

  @Override
  public boolean hasNext()
  {
    return (i < array.length);
  }

  @Override
  public T next()
  {
    return array[i++];
  }

  @Override
  public void remove()
  {
    throw new UnsupportedOperationException("Remove is not supported.");
  }



}
