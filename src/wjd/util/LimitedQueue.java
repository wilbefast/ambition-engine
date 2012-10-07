package wjd.util;

import java.util.LinkedList;

/**
 * @author GreyCat
 * @since Mar-31-11
 * @see Stack Overflow <a href="http://stackoverflow.com/questions/5498865/size-limited-queue-that-holds-last-n-elements-in-java>
 * "Size-limited queue that holds last N elements in Java"</a>
 */
public class LimitedQueue<T> extends LinkedList<T>
{
  /* ATTRIBUTES */
  private int limit;

  /* METHODS */
  public LimitedQueue(int limit)
  {
    this.limit = limit;
  }

  @Override
  public boolean add(T object)
  {
    // add the object
    super.add(object);
    // clear the oldest elements
    while (size() > limit) 
      super.remove();
    // always successful!
    return true;
  }
}
