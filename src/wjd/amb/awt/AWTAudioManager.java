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

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import wjd.amb.resources.AAudioManager;
import wjd.amb.resources.ISound;

/**
 *
 * @author wdyce
 * @since Nov 10, 2012
 */
public class AWTAudioManager extends AAudioManager
{
  /* IMPLEMENTS -- AAUDIOMANAGER */
  
  @Override
  protected ISound loadSound(String filename, AudioFileType type)
  {
    try
    {
      Clip clip = AudioSystem.getClip();
      //AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(filename));
      //clip.open(inputStream);
      AWTSound new_sound = new AWTSound(clip);
      return new_sound;
    }
    /*catch (IOException ex)
    {
      Logger.getLogger(AWTAudioManager.class.getName()).log(Level.SEVERE, null, ex);
    }*/
    catch (LineUnavailableException ex)
    {
      Logger.getLogger(AWTAudioManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    /*catch (UnsupportedAudioFileException ex)
    {
      Logger.getLogger(AWTAudioManager.class.getName()).log(Level.SEVERE, null, ex);
    }*/
    finally
    {
      return null;
    }
  }
}
