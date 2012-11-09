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

import java.awt.Font;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import wjd.amb.view.Colour;
import wjd.amb.view.ICamera;
import wjd.amb.view.ICanvas;
import wjd.math.Rect;
import wjd.math.V2;

/**
 * A collection of drawing functions to render primitives (circles, boxes and
 * so-on) using OpenGL.
 *
 * @author wdyce
 * @since 16-Feb-2012
 */
public class LWJGLCanvas implements ICanvas
{
  /* CONSTANTS */
  private static final int CIRCLE_BASE_SEGMENTS = 6;
  private static final int CIRCLE_MAX_SEGMENTS = 20;
  
  /* SINGLETON */
  private static LWJGLCanvas instance;
  public static LWJGLCanvas getInstance()
  {
    if(instance == null)
      instance = new LWJGLCanvas();
    return instance;
  }
  
  /* ATTRIBUTES */
  private org.newdawn.slick.Color slickColour = Color.black;
  private org.newdawn.slick.Font font;
  private boolean use_camera;
  private ICamera camera;
  private V2 size = new V2();

  /* METHODS */
  // creation
  /**
   * Set up the OpenGL state machine for drawing, including setting clear colour
   * and depth, and enabling/disabling various options (namely 3D).
   */
  private LWJGLCanvas()
  {
    // background colour and depth
    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    glClearDepth(1.0f);

    // 2d initialisation
    glDisable(GL_DEPTH_TEST);
    glDisable(GL_LIGHTING);
    glEnable(GL_TEXTURE_2D);

    // we need blending (alpha) for drawing text
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    // load a default font
    font = new TrueTypeFont(new java.awt.Font("Arial", Font.PLAIN, 12), false);
  }
  
  /* IMPLEMENTATION -- ICANVAS */
  
  // query
  
  @Override
  public ICamera getCamera()
  {
    return camera;
  }
  
  @Override
  public boolean isCameraActive()
  {
    return use_camera;
  }
  
  // modify the canvas itself
  @Override
  public ICanvas setSize(V2 size)
  {
    // reset size
    this.size = size;
    
    // reset camera
    if(use_camera)
      camera.setProjectionSize(size);
    
    //Here we are using a 2D Scene
    glViewport(0, 0, (int)size.x, (int)size.y);
    // projection
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, size.x, size.y, 0, -1, 1);
    glPushMatrix();
    // model view
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
    
    return this;
  }
  
  @Override
  public ICanvas setCamera(ICamera camera)
  {
    // attach camera and reset its field size
    this.camera = camera;
    
    // maintain invariant: (camera == null) => use_camera = false
    if(!use_camera && camera != null)
    {
      use_camera = true;
      camera.setProjectionSize(size);
    }
    else if(camera == null)
      use_camera = false;
    
    return this;
  }
  
  // modify the paintbrush state
  @Override
  public ICanvas setColour(Colour colour)
  {
    slickColour = new Color(colour.r, colour.g, colour.b, colour.a);
    glColor4f(colour.r, colour.g, colour.b, colour.a);
    return this;
  }

  @Override
  public ICanvas setLineWidth(float lineWidth)
  {
    glLineWidth(lineWidth);
    return this;
  }

  @Override
  public ICanvas setCanvasFont(java.awt.Font awt_font)
  {
    font = new TrueTypeFont(awt_font, false); // not anti-aliasing
    return this;
  }
  
  @Override
  public ICanvas setFontSize(int new_size)
  {
    font = new TrueTypeFont(new Font("Arial", Font.PLAIN, new_size), false);
    return this;
  }
  
  @Override
  public ICanvas toggleCamera(boolean use_camera)
  {
    // maintain invariant: (camera == null) => use_camera = false
    if(!use_camera || camera != null)
      this.use_camera = use_camera;
    
    return this;
  }

  // drawing functions
  @Override
  public void clear()
  {
    glClear( GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT );
    glLoadIdentity();
  }
  
  @Override
  public void circle(V2 centre, float radius, boolean fill)
  {
    // move based on camera position where applicable
    V2 pov_centre;
    if(use_camera) 
    {
      radius *= camera.getZoom();
      pov_centre = camera.getPerspective(centre);
    }
    else
      pov_centre = centre;
    
    // draw the circle
    int deg_step = (int) (360 / 
                (Math.min(CIRCLE_BASE_SEGMENTS * radius, CIRCLE_MAX_SEGMENTS)));
    glBegin((fill) ? GL_TRIANGLE_FAN : GL_LINE_LOOP);
      if(fill) glVertex2f(pov_centre.x, pov_centre.y);
      for (int deg = 0; deg < 360 + deg_step; deg += deg_step)
      {
        double rad = deg * Math.PI / 180;
        glVertex2f((float) (pov_centre.x + Math.cos(rad) * radius),
          (float) (pov_centre.y + Math.sin(rad) * radius));
      }
    glEnd();
  }

  @Override
  public void line(V2 start, V2 end)
  {
    // move based on camera position where applicable
    V2 pov_start = (use_camera) ? camera.getPerspective(start) : start;
    V2 pov_end = (use_camera) ? camera.getPerspective(end) : end;
    
    glBegin(GL_LINES);
      glVertex2d(pov_start.x, pov_start.y);
      glVertex2d(pov_end.x, pov_end.y);
    glEnd();
  }

  @Override
  public void box(Rect rect, boolean fill)
  {
    // move based on camera position where applicable
    Rect pov_rect = (use_camera) ? camera.getPerspective(rect) : rect;
    
    glBegin((fill) ? GL_QUADS : GL_LINE_LOOP);
      glVertex2f(pov_rect.x, pov_rect.y);
      glVertex2f(pov_rect.x + pov_rect.w, pov_rect.y);
      glVertex2f(pov_rect.x + pov_rect.w, pov_rect.y + pov_rect.h);
      glVertex2f(pov_rect.x, pov_rect.y + pov_rect.h);
    glEnd();
  }

  @Override
  public void text(String string, V2 position)
  {
    // move based on camera position where applicable
    V2 pov_pos = (use_camera) ? camera.getPerspective(position) : position;
    
    glEnable(GL_BLEND);
      font.drawString(pov_pos.x, pov_pos.y, string, slickColour);
    glDisable(GL_BLEND);
  }
}
