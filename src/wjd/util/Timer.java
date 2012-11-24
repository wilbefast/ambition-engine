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

import java.io.Serializable;
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IDynamic;

/**
 *
 * @author wdyce
 * @since Nov 24, 2012
 */
public class Timer extends BoundedValue implements IDynamic, Serializable 
{
  /* METHODS */
  
  // constructors
  public Timer(int milliseconds)
  {
    super((int)(Math.random()*milliseconds), milliseconds);
  }
  
  /* IMPLEMENTS -- IDYNAMIC */
  
  @Override
  public EUpdateResult update(int t_delta)
  {
    if(tryDeposit(t_delta) < t_delta)
    {
      empty();
      return EUpdateResult.FINISHED;
    }
    
    return EUpdateResult.CONTINUE;
  }
}
