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

package wjd.amb.view;

import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IDynamic;
import wjd.amb.resources.Animation;
import wjd.math.Rect;

/**
 * 
 * @author wjd
 * @since jan-2012
 */
public class AnimationCanvas extends GraphicCanvas implements IDynamic
// implements IVisible via GraphicCanvas
{
  /* ATTRIBUTES -- inherited */
  // private Graphic graphic;
  // private iRect dest;

  /* ATTRIBUTES */
  private float current_frame = 0;
  private float frame_speed;

  /* METHODS */
  // creation
  public AnimationCanvas(Animation init_anim, Rect init_dest, float frame_speed)
  {
    super(init_anim, init_dest);
    this.frame_speed = frame_speed;
  }

  // update
  @Override
  public EUpdateResult update(int t_delta)
  {
    // Increment frame
    current_frame += frame_speed*t_delta;

    // Detect if we're over the maximum number of frames
    Animation animation = ((Animation) graphic);
    int frame_number = animation.getNumFrames();
    if (current_frame >= frame_number || current_frame < 0)
    {
      switch (animation.getLoopType())
      {
        case STOP_AT_END:
          current_frame = frame_number - 1;
          frame_speed = 0;
          break;

        case STOP_AT_START:
          current_frame = 0;
          frame_speed = 0;
          break;

        case WRAP_AROUND:
          current_frame -= Math.signum(frame_speed) * frame_number;
          break;

        case ALTERNATE_DIRECTION:
          current_frame = (current_frame < 0)
                          ? 0 : frame_number - 1 - frame_speed;
          frame_speed *= -1;
          break;

      }
    }
    
    // all clear
    return EUpdateResult.CONTINUE;
  }
  
  /* SUBROUTINES */
  @Override
  protected Rect getSubrect()
  {
    // offset the frame based on current animation subimage
    return ((Animation) graphic).getFrame(current_frame);
  }
}
