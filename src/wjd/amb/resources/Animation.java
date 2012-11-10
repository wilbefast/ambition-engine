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

import java.awt.image.BufferedImage;
import wjd.math.Rect;

/**
 * 
 * @author wjd
 * @since jan-2012
 */
public class Animation extends Graphic
{
  /// NESTED TYPES

  public static enum LoopType
  {

    STOP_AT_END,
    STOP_AT_START,
    WRAP_AROUND,
    ALTERNATE_DIRECTION;
  };
  /// ATTRIBUTES -- INHERITED
  // private BufferedImage image;
  // private iRect frame;
  /// ATTRIBUTES
  private Rect strip;
  private int numFrames;
  private LoopType loopType;

  /// METHODS
  // creation
  public Animation(BufferedImage init_image, Rect init_frame,
                   int init_numFrames)
  {
    super(init_image, init_frame);
    numFrames = init_numFrames;
    strip = new Rect(frame.x, frame.y, frame.w * numFrames, frame.h);
  }

  public Animation(BufferedImage init_image, Rect init_frame,
                   int init_numFrames, LoopType init_loopType)
  {
    this(init_image, init_frame, init_numFrames);
    loopType = init_loopType;
  }

  public Rect getFrame(double frame_number)
  {
    if (frame_number == 0)
      return getFrame();

    // check that the requested area is not out of bounds
    int offset = (int) (Math.floor(frame_number) * frame.w);
    if (offset + frame.w > strip.w)
      return frame;

    // return a translated version of the frame
    Rect result = frame.clone();
    result.shift(offset, 0);
    return result;
  }

  public int getNumFrames()
  {
    return numFrames;
  }

  public LoopType getLoopType()
  {
    return loopType;
  }
}
