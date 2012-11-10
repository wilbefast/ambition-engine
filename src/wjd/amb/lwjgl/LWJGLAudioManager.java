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
package wjd.amb.lwjgl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;
import wjd.amb.resources.AAudioManager;
import wjd.amb.resources.ISound;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class LWJGLAudioManager extends AAudioManager
{
  /* IMPLEMENTS -- AAUDIOMANAGER */
  
  @Override
  protected ISound loadSound(String filename, AudioFileType type)
  {
    try
    {
      LWJGLSound new_sound = new LWJGLSound(AudioLoader.getAudio(type.toString(), 
                                ResourceLoader.getResourceAsStream(filename)));
      return new_sound;
    }
    catch (IOException ex)
    {
      Logger.getLogger(LWJGLAudioManager.class.getName()).log(Level.SEVERE, null, ex);
      return null;
    }
  }
}
