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

/**
 *
 * @author wdyce
 * @since 16 Feb, 2012
 */
class SearchState implements Comparable<SearchState>
{
  /* ATTRIBUTES */
  
  public final PathSearch search;
  public final Tile tile;
  public SearchState previous = null;
  public int currentCost = 0;
  public int totalCostEstimate = 0;
  public boolean closed = false;
  

  /* METHODS */
  
  // constructors
  public SearchState(Tile current, PathSearch search)
  {
    this.tile = current;
    this.search = search;
  }

  public void setParent(SearchState previous)
  {
    this.previous = previous;
    currentCost = previous.currentCost + 1;
    totalCostEstimate = currentCost + search.estimateCost(tile);
  }

  @Override
  public int compareTo(SearchState other)
  {
    int delta = totalCostEstimate - other.totalCostEstimate;
    // return different in heuristics if ever total cost are the same
    return (delta != 0)
           ? delta
           : search.estimateCost(tile) - search.estimateCost(other.tile);
  }
}
