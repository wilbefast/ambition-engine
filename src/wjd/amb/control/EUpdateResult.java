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
   * Command to quit the program or return from the current loop.
   */
  EXIT,
  /**
   * Switch to a new IDynamic object which should be specified in an 
   * "IDynamic getNext()" type method.
   */
  REPLACE_ME,
  /**
   * The updated object should be deleted from the container.
   */
  DELETE_ME,
  /**
   * The updated object should be moved to a different container specified in an
   * "Object getNewContainer()" method.
   */
  MOVE_ME,
  /**
   * The updated object has finished its appointed task.
   */
  FINISHED,
  /**
   * The updated object is waiting for something so that it can complete its 
   * task.
   */
  BLOCKED,
  /**
   * The updated object wishes to cancel its appointed task.
   * 
   */
  CANCEL
};
