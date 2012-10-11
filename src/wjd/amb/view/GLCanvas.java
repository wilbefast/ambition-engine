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

import java.awt.Font;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.math.Rect;
import wjd.math.V2;

/**
 * A collection of drawing functions to render primitives (circles, boxes and
 * so-on) using OpenGL.
 *
 * @author wdyce
 * @since 16-Feb-2012
 */
public class GLCanvas implements ICanvas
{
  /* CONSTANTS */
  private static final int CIRCLE_BASE_SEGMENTS = 6;
  
  /* SINGLETON */
  private static GLCanvas instance;
  public static GLCanvas getInstance()
  {
    if(instance == null)
      instance = new GLCanvas();
    return instance;
  }
  
  /* ATTRIBUTES */
  private org.newdawn.slick.Color slickColour = Color.black;
  private TrueTypeFont font;
  private boolean use_camera;
  private Camera camera;
  private V2 size = new V2();
  // temporary objects, to avoid to many calls to 'new'
  private V2 tmpV2a = new V2(), tmpV2b = new V2();
  private Rect tmpRect = new Rect();

  /* METHODS */
  // creation
  /**
   * Set up the OpenGL state machine for drawing, including setting clear colour
   * and depth, and enabling/disabling various options (namely 3D).
   */
  private GLCanvas()
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
    font = new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), false);
  }
  
  /* IMPLEMENTATION -- ICANVAS */
  
  // query
  
  @Override
  public Camera getCamera()
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
      camera.setCanvasSize(size);
    
    //Here we are using a 2D Scene
    glViewport(0, 0, (int)size.x(), (int)size.y());
    // projection
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, size.x(), size.y(), 0, -1, 1);
    glPushMatrix();
    // model view
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    glPushMatrix();
    
    return this;
  }
  
  @Override
  public ICanvas setCamera(Camera camera)
  {
    this.camera = camera;
    
    // maintain invariant: (camera == null) => use_camera = false
    if(!use_camera && camera != null)
      use_camera = true;
    else if(camera == null)
      use_camera = false;
    
    return this;
  }
  
  @Override
  public ICanvas createCamera(Rect boundary)
  {
    return setCamera(new Camera(size, boundary));
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
  public ICanvas setFont(Object new_font)
  {
    if (new_font instanceof TrueTypeFont)
      font = (TrueTypeFont) new_font;
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
  /**
   * Clear the screen.
   */
  @Override
  public void clear()
  {
    glClear( GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT );
    glLoadIdentity();
  }
  
  /**
   * Draw a circle outline around the specified position, using the given
   * radius.
   *
   * @param centre vector position corresponding to the centre of the circle.
   * @param radius the size of the circle from centre to outskirts.
   */
  @Override
  public void circle(V2 centre, float radius)
  {
    // move based on camera position where applicable
    radius *= camera.getZoom();
    tmpV2a = (use_camera) ? camera.getPerspective(centre) : centre;
    
    // draw the circle
    int deg_step = (int) (360 / (CIRCLE_BASE_SEGMENTS * radius));
    glBegin(GL_TRIANGLE_FAN);
    glVertex2f(tmpV2a.x(), tmpV2a.y());
    for (int deg = 0; deg < 360 + deg_step; deg += deg_step)
    {
      double rad = deg * Math.PI / 180;
      glVertex2f((float) (tmpV2a.x() + Math.cos(rad) * radius),
        (float) (tmpV2a.y() + Math.sin(rad) * radius));
    }
    glEnd();
  }

  /**
   * Draw a straight black line between the two specified points.
   *
   * @param start vector position corresponding to the start of the line.
   * @param end vector position corresponding to the end of the line.
   */
  @Override
  public void line(V2 start, V2 end)
  {
    // move based on camera position where applicable
    tmpV2a = (use_camera) ? camera.getPerspective(start) : start;
    tmpV2b = (use_camera) ? camera.getPerspective(end) : end;
    
    glBegin(GL_LINES);
      glVertex2d(tmpV2a.x(), tmpV2a.y());
      glVertex2d(tmpV2b.x(), tmpV2b.y());
    glEnd();
  }

  /**
   * Draw the outline of a Rectangle.
   *
   * @param rect the rectangle object whose outline will be drawn.
   */
  @Override
  public void box(Rect rect)
  {
    glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
    // move based on camera position where applicable
    tmpRect = (use_camera) ? camera.getPerspective(rect) : rect;
    
    glBegin(GL_QUADS);
      glVertex2f(tmpRect.x(), tmpRect.y());
      glVertex2f(tmpRect.x() + tmpRect.w(), tmpRect.y());
      glVertex2f(tmpRect.x() + tmpRect.w(), tmpRect.y() + tmpRect.h());
      glVertex2f(tmpRect.x(), tmpRect.y() + tmpRect.h());
    glEnd();
  }

  /**
   * Draw a String of characters at the indicated position.
   *
   * @param string the String of characters to be drawn.
   * @param position the position on the screen to draw the String.
   */
  @Override
  public void text(String string, V2 position)
  {
    // move based on camera position where applicable
    tmpV2a = (use_camera) ? camera.getPerspective(position) : position;
    
    glEnable(GL_BLEND);
    font.drawString(tmpV2a.x(), tmpV2a.y(), string, slickColour);
    glDisable(GL_BLEND);
  }
  
  /* IMPLEMENTATION -- IINTERACTIVE */

  @Override
  public EUpdateResult processInput(IInput input)
  {
    // check whether we should stop running
    if(Display.isCloseRequested())
      return EUpdateResult.STOP;
    
    // update the Camera
    return (use_camera) 
          ? camera.processInput(input) 
          : EUpdateResult.CONTINUE;
  }
}
