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
package wjd.amb.resources;

import java.util.HashMap;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public abstract class AAudioManager 
{
  /* NESTING */
  public static enum AudioFileType
  {
    // values
    OGG 
    {
      @Override
      public String toString() { return ".ogg"; } 
    },
    WAV 
    {
      @Override
      public String toString() { return ".wav"; } 
    };
  }
  
  /* ATTRIBUTES */
  private HashMap<String, ISound> sounds;
  
  /* METHODS */

  // constructors
  public AAudioManager()
  {
    sounds = new HashMap<String, ISound>();
  }

  // accessors
  public ISound getSound(String image_name)
  {
    return sounds.get(image_name);
  }
  
  // mutators
  public void addSound(String name, AudioFileType type)
  {
    
    ISound new_sound = loadSound("res/sfx/"+ name + type, type);
    if(new_sound != null)
      sounds.put(name, new_sound);
  }
  
  protected abstract ISound loadSound(String string, AudioFileType type);
}
