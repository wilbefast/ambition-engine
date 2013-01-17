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
import wjd.math.Rect;

/**
 *
 * @author wjd
 * @since jan-2012
 */
public abstract class ATextureManager
{
  /* NESTING */
  public static enum ImageFileType
  {
    // values
    PNG 
    {
      @Override
      public String toString() { return ".png"; } 
    },
    JPG 
    {
      @Override
      public String toString() { return ".jpg"; } 
    };
  }
  
  /* ATTRIBUTES */
  private HashMap<String, ITexture> textures;
  private HashMap<String, Graphic> graphics;
  private HashMap<String, Animation> animations;
  private HashMap<String, Tileset> tilesets;

  /* METHODS */
  // creation
  public ATextureManager()
  {
    textures = new HashMap<String, ITexture>();
    graphics = new HashMap<String, Graphic>();
    animations = new HashMap<String, Animation>();
    tilesets = new HashMap<String, Tileset>();
  }

  // accessors
  public ITexture getTexture(String texture_name)
  {
    return textures.get(texture_name);
  }

  public Graphic getGraphic(String graphic_name)
  {
    return graphics.get(graphic_name);
  }

  public Animation getAnimation(String animation_name)
  {
    return animations.get(animation_name);
  }
  
  public Tileset getTileset(String tileset_name)
  {
    return tilesets.get(tileset_name);
  }

  // mutators
  public void addTexture(String name, ImageFileType type)
  {
    
    ITexture new_texture = loadTexture("res/gfx/"+ name + type, type);
    if(new_texture != null)
      textures.put(name, new_texture);
  }

  public void addGraphic(String graphic_name, String texture_name,
                            Rect frame)
  {
    graphics.put(graphic_name, new Graphic(textures.get(texture_name), frame));
  }

  public void addAnimation(String anim_name, String texture_name,
                              Rect frame, int n_frames, Animation.LoopType loopType)
  {
    Animation new_anim = new Animation(getTexture(texture_name), frame,
                                       n_frames, loopType);
    animations.put(anim_name, new_anim);
  }
  
  public void addTileset(String tileset_name, String texture_name,
                              Rect frame, int n_across, int n_high)
  {
    Tileset new_tileset = new Tileset(getTexture(texture_name), frame,
                                       n_across, n_high);
    tilesets.put(tileset_name, new_tileset);
  }
  
    
  /* INTERFACE */
  
  protected abstract ITexture loadTexture(String filename, ImageFileType type);
}
