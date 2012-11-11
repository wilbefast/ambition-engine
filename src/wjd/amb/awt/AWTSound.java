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
package wjd.amb.awt;
import javax.sound.sampled.Clip;
import wjd.amb.resources.ISound;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
class AWTSound implements ISound 
{
  /* NESTING */
  private static class AWTSoundInstance implements Runnable
  {
    // attributes
    private Clip clip;
    
    // constructors
    public AWTSoundInstance(Clip clip)
    {
      this.clip = clip;
    }
    
    // implements -- runnable
    
    @Override
    public void run()
    {
      clip.start(); 
    }
    
  }
  
  /* ATTRIBUTES */
  private Clip awt_audio;
  
  /* METHODS */

  // constructors
  AWTSound(Clip awt_audio)
  {
    this.awt_audio = awt_audio;
  }
  
  // accessors

  // mutators
}
