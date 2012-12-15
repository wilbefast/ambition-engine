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

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import wjd.amb.rts.Tile;
import wjd.amb.rts.TileGrid;

/**
 *
 * @author wdyce
 * @since 16 Feb, 2012
 */
public class PathSearch
{
  /* NESTING */
  
  /* ATTRIBUTES */
  private final TileGrid grid;
  private final SearchState start;
  private final SearchState end;
  private final AHeuristic heuristic = AHeuristic.EUCLIEAN;
  private Map<Tile, SearchState> states;
  private Queue<SearchState> open;
  private boolean hasResult = false;
  private SearchState fallback_plan;
  

  /* METHODS */
  
  // constructors
  
  public PathSearch(Tile start_tile, Tile end_tile)
  {
    // initialise final attributes
    this.grid = start_tile.grid;
    
    this.start = new SearchState(start_tile, this);
    this.end = new SearchState(end_tile, this);

    fallback_plan = start;
    start.totalCostEstimate = estimateCost(start_tile);
    
    // wrap graph vertices in exploration state objects
    states = new HashMap<Tile, SearchState>();
    states.put(start_tile, start);
    states.put(end_tile, end);

    // add the start state to the open set
    open = new PriorityQueue<SearchState>();
    open.add(start);
    
    // perform the search
    hasResult = search();
  }
  
  // accessors
  
  final int estimateCost(Tile tile)
  {
    return heuristic.estimate(tile.grid_position, end.tile.grid_position);
  }
  
  public Deque<Tile> getPath()
  {
    Deque<Tile> result = new LinkedList<Tile>();

    // start at the end, trace backwards adding vertices
    SearchState current = hasResult ? end : fallback_plan;
    while (current != start)
    {
      // add element to front, in order for list to be in the right order
      result.addFirst(current.tile);
      current = current.previous;
    }
    return result;
  }
  
  /* SUBROUTINES */

  private boolean search()
  {
    while (!open.isEmpty())
    {
      // expand from the open state that is currently cheapest
      SearchState x = open.poll();

      // have we reached the end?
      if (x.equals(end))
        return true;

      // try to expand each neighbour
      for (Tile t : grid.getNeighbours(x.tile, false))
        if(t.isPathable())
          expand(x, t);

      // remember to close x now that all connections have been expanded
      x.closed = true;
      
      // keep the best closed state, just in case the target is inaccessible
      if(estimateCost(x.tile) < estimateCost(fallback_plan.tile))
        fallback_plan = x;
    }
    
    // fail!
    return false;
  }

  private void expand(SearchState src_state, Tile t)
  {
    SearchState dest_state = states.get(t);
    
    // create states as needed
    if(dest_state == null)
    {
      dest_state = new SearchState(t, this);
      states.put(t, dest_state);
    }

    // closed states are no longer under consideration
    else if (dest_state.closed)
      return;

    // states not yet opened always link back to x
    if (!open.contains(dest_state))
    {
      // set cost before adding to heap, or order will be wrong!
      dest_state.setParent(src_state);
      open.add(dest_state);
    }
    // states already open link back to x only if it's better
    else if (src_state.currentCost < dest_state.currentCost)
    {
      // remove, reset cost and replace, or order will be wrong!
      open.remove(dest_state);
      dest_state.setParent(src_state);
      open.add(dest_state);
    }
  }
}
