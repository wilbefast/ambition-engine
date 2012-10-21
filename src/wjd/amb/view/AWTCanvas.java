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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JPanel;
import wjd.amb.control.EUpdateResult;
import wjd.amb.control.IInput;
import wjd.math.Rect;
import wjd.math.V2;

/** 
 * 
 * @author wdyce 
 * @since 25 Jan, 2012
 */
public class AWTCanvas extends JPanel implements ICanvas
{
  /* UTILITY */
  private static Color ColourToAWTColor(Colour c)
  {
    return new Color(c.r, c.g, c.b, c.a);
  }
  
  /* POSSIBLE DRAW COMMANDS TO BE QUEUED */
  private static interface DrawCommand { }
  
  private static class DrawShape implements DrawCommand
  {
    public Shape shape;
    public DrawShape(Shape shape) { this.shape = shape; }
  }
  private static class DrawText implements DrawCommand
  {
    public String text;
    public V2 pos;
    public DrawText(String text, V2 pos) { this.text = text; this.pos = pos; }
  }
  private static class ColourChange implements DrawCommand
  {
    public Colour colour;
    public ColourChange(Colour colour) { this.colour = colour; }
  }
  private static class LineWidthChange implements DrawCommand
  {
    public float lineWidth;
    public LineWidthChange(float lineWidth) { this.lineWidth = lineWidth; }
  }
  private static class FontChange implements DrawCommand
  {
    public Font font;
    public FontChange(Font font) { this.font = font; }
  }
  private static class FontSizeChange implements DrawCommand
  {
    public int size;
    public FontSizeChange(int size) { this.size = size; }
  } 
  private static class ToggleCamera implements DrawCommand
  {
    public boolean active;
    public ToggleCamera(boolean active) { this.active = active; }
  }
  
  /* ATTRIBUTES */
  private Queue<DrawCommand> draw_queue;
  private Camera camera = null;
  private V2 size = new V2();
  private boolean use_camera = false;
  // temporary objects, to avoid to many calls to 'new'
  private V2 tmpV2a = new V2(), tmpV2b = new V2();
  private Rect tmpRect = new Rect();

  /* METHODS */
    
  // creation
  /**
   * Create the JPanel.
   */
  public AWTCanvas()
  {
    draw_queue = new LinkedList<DrawCommand>();
  }
  
  /* IMPLEMENTATIONS -- ICANVAS */
  
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
    draw_queue.add(new ColourChange(colour));
    return this;
  }

  @Override
  public ICanvas setLineWidth(float lineWidth)
  {
    draw_queue.add(new LineWidthChange(lineWidth));
    return this;
  }

  @Override
  public ICanvas setCanvasFont(Font new_font)
  {
    draw_queue.add(new FontChange(new_font));
    return this;
  }
  
  @Override
  public ICanvas setFontSize(int size)
  {
    draw_queue.add(new FontSizeChange(size));
    return this;
  }
  
  @Override
  public ICanvas toggleCamera(boolean use_camera)
  {
    draw_queue.add(new ToggleCamera(use_camera));
    return this;
  }

  // drawing functions
  /**
   * Clear the screen.
   */
  @Override
  public void clear()
  {
    // empty the list of draw_queue
    draw_queue.clear();
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
    tmpV2a = (use_camera) ? camera.getPerspective(centre) : centre;
    
    draw_queue.add(new DrawShape(new Ellipse2D.Float(tmpV2a.x(), tmpV2a.y(), 
                                                    radius*2, radius*2)));
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
    
    draw_queue.add(new DrawShape(new Line2D.Float(tmpV2a.x(), tmpV2a.y(), 
                                    tmpV2b.x(), tmpV2b.y())));
  }

  /**
   * Draw the outline of a Rectangle.
   *
   * @param rect the rectangle object whose outline will be drawn.
   */
  @Override
  public void box(Rect rect)
  {
    // move based on camera position where applicable
    tmpRect = (use_camera) ? camera.getPerspective(rect) : rect;
    
    draw_queue.add(new DrawShape(new Rectangle2D.Float(tmpRect.x(), tmpRect.y(), 
                                                    tmpRect.w(), tmpRect.h())));
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
    
    draw_queue.add(new DrawText(string, tmpV2a));
  }
  
  @Override
  public void paintComponent(Graphics g)
  {
    // Get the graphics object
    Graphics2D g2d = (Graphics2D)g;
    
    // Clear the screen in white
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    // Draw each shape in black by default
    g2d.setColor(Color.BLACK);
    Object command;
    while((command = draw_queue.poll()) != null)
    {
      // draw a shape
      if(command instanceof DrawShape)
        g2d.fill(((DrawShape)command).shape);
      // draw text
      else if(command instanceof DrawText)
      {
        DrawText text_cmd = (DrawText)command;
        g2d.drawString(text_cmd.text, text_cmd.pos.x(), text_cmd.pos.y());
      }
      // change colour
      else if(command instanceof ColourChange)
        g2d.setColor(ColourToAWTColor(((ColourChange)command).colour));
      // change line width
      else if(command instanceof LineWidthChange)
        g2d.setStroke(new BasicStroke(((LineWidthChange)command).lineWidth));
      // change new_font
      else if(command instanceof FontChange)
        g2d.setFont((Font)(((FontChange)command).font));
      // activate or deactivate camera
      else if(command instanceof ToggleCamera)
      {
      // maintain invariant: (camera == null) => use_camera = false
      if(!use_camera || camera != null)
        this.use_camera = ((ToggleCamera)command).active;
      }
      // unknown or unsupported command
      else
        System.out.println("Unrecognized command type " + command);
    }
  }
  
  /* IMPLEMENTATION -- IINTERACTIVE */

  @Override
  public EUpdateResult processInput(IInput input)
  {
    return (use_camera) ? camera.processInput(input) : EUpdateResult.CONTINUE;
  }
}