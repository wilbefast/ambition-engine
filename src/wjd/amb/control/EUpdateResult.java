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
package wjd.amb.control;

/**
 * A value of this type is returned when certain objects are updated.
 *
 * @author wdyce
 * @since 01-Oct-2012
 */
public enum EUpdateResult
{
  /**
   * The default result of an update: continue as before.
   */
  CONTINUE,
  /**
   * The object calling the update should stop doing so and either switch it for
   * another one or exit.
   */
  STOP,
  /**
   * The updated object should be deleted.
   */
  DELETE_ME
};
