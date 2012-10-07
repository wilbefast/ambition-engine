package wjd.amb.control;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

/**
 * On Unix Systems the OS generates "phantom" key-pressed and -released events
 * when users hold down keys: obviously we don't want this in a game.
 *
 * @author Endre Stølsvik
 */
public class KeyRepeatFix implements AWTEventListener
{
  /// NESTED DECLARATIONS
  public interface Reposted
  { /* marker only */ }

  public static class RepostedKeyEvent extends KeyEvent implements Reposted
  {
    public RepostedKeyEvent(@SuppressWarnings("hiding") Component source,
                            @SuppressWarnings("hiding") int id, long when,
                            int modifiers,
                            int keyCode, char keyChar, int keyLocation)
    {
      super(source, id, when, modifiers, keyCode, keyChar, keyLocation);
    }
  }

  // ActionListener which reports real release events
  private class ReleasedAction implements ActionListener
  {
    /// ATTRIBUTES
    private final KeyEvent originalKEvent;
    private Timer timer;

    /// METHODS
    public ReleasedAction(KeyEvent originalReleased, Timer init_timer)
    {
      this.timer = init_timer;
      originalKEvent = originalReleased;
    }

    public void cancel()
    {
      assert assertEDT();
      timer.stop();
      timer = null;
      key_states.remove(Integer.valueOf(originalKEvent.getKeyCode()));
    }

    @Override
    public void actionPerformed(@SuppressWarnings("unused") ActionEvent e)
    {
      assert assertEDT();
      // Judging by Timer and TimerQueue code, we can theoretically be
      // raced to be posted onto EDT by TimerQueue, due to some lag,
      // unfair scheduling
      if (timer == null)
        return;
      // Stop Timer and clean.
      cancel();

      // Create new KeyEvent (we've consumed the original).
      KeyEvent newEvent = new RepostedKeyEvent(
        (Component) originalKEvent.getSource(),
        originalKEvent.getID(),
        originalKEvent.getWhen(),
        originalKEvent.getModifiers(),
        originalKEvent.getKeyCode(),
        originalKEvent.getKeyChar(),
        originalKEvent.getKeyLocation());

      // Post to EventQueue.
      Toolkit.getDefaultToolkit().getSystemEventQueue().
        postEvent(newEvent);
    }
  }
  /// CLASS NAMESPACE VARIABLES
  private static KeyRepeatFix installed;

  /// CLASS NAMESPACE FUNCTIONS
  // singleton creation and destruction
  public static void install()
  {
    if (installed == null)
    {
      installed = new KeyRepeatFix();
      Toolkit.getDefaultToolkit().
        addAWTEventListener(installed, AWTEvent.KEY_EVENT_MASK);
    }
  }

  public static void remove()
  {
    if (installed != null)
      Toolkit.getDefaultToolkit().removeAWTEventListener(installed);
  }

  // utilities
  private static boolean assertEDT()
  {
    if (EventQueue.isDispatchThread())
      return true;
    throw new AssertionError("Must be Event Dispatch Thread (EDT)");
  }
  /// ATTRIBUTES
  private final Map<Integer, ReleasedAction> key_states =
    new HashMap<Integer, ReleasedAction>();

  /// METHODS
  // overrides
  @Override
  public void eventDispatched(AWTEvent event)
  {
    // Make sure we're called with keyboard events
    assert event instanceof KeyEvent : "Must be a KeyEvent";
    assert assertEDT();
    // Ignore repeated keyboard events
    if (event instanceof Reposted)
      return;
    // Ignore typing events
    if (event.getID() == KeyEvent.KEY_TYPED)
      return;
    // Ignore consumed events
    final KeyEvent keyEvent = (KeyEvent) event;
    if (keyEvent.isConsumed())
      return;

    /**
     * Deal with repeated KEY_RELEASED *
     */
    if (keyEvent.getID() == KeyEvent.KEY_RELEASED)
    {
      final Timer timer = new Timer(2, null);
      ReleasedAction action = new ReleasedAction(keyEvent, timer);
      timer.addActionListener(action);
      timer.start();

      key_states.put(Integer.valueOf(keyEvent.getKeyCode()), action);

      // Consume the original
      keyEvent.consume();

      // We can stop now
      return;
    }

    /**
     * Deal with repeated KEY_PRESSED *
     */
    if (keyEvent.getID() == KeyEvent.KEY_PRESSED)
    {
      // Pick out the corresponding release action
      ReleasedAction action =
        key_states.remove(Integer.valueOf(keyEvent.getKeyCode()));
      // Dump it if it exists
      if (action != null)
        action.cancel();
    }
  }
}