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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.swing.JPanel;
import wjd.amb.resources.ITexture;
import wjd.amb.view.Colour;
import wjd.amb.view.ICamera;
import wjd.amb.view.ICanvas;
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
    public boolean fill;
    
    // methods
    public DrawShape(Shape shape, boolean fill) { this.shape = shape; 
                                                  this.fill = fill; }
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final DrawShape other = (DrawShape) obj;
      if (!Objects.equals(this.shape, other.shape))
        return false;
      if (this.fill != other.fill)
        return false;
      return true;
    }
    @Override
    public int hashCode()
    {
      int hash = 5;
      hash = 71 * hash + Objects.hashCode(this.shape);
      hash = 71 * hash + (this.fill ? 1 : 0);
      return hash;
    }
  }
  private static class DrawText implements DrawCommand
  {
    public String text;
    public V2 pos;
    
    // methods
    public DrawText(String text, V2 pos) 
    { 
      this.text = text; this.pos = pos.clone(); 
    }
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final DrawText other = (DrawText) obj;
      if (!Objects.equals(this.text, other.text))
        return false;
      if (!Objects.equals(this.pos, other.pos))
        return false;
      return true;
    }
    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 29 * hash + Objects.hashCode(this.text);
      hash = 29 * hash + Objects.hashCode(this.pos);
      return hash;
    }
  }
  private static class DrawImage implements DrawCommand
  {
    public Rect source, dest;
    public BufferedImage image;
    
    // methods
    public DrawImage(Rect source, Rect dest, BufferedImage image)
    {
      this.source = source; this.dest = dest; this.image = image;
    }

    @Override
    public int hashCode()
    {
      int hash = 5;
      hash = 89 * hash + Objects.hashCode(this.source);
      hash = 89 * hash + Objects.hashCode(this.dest);
      hash = 89 * hash + Objects.hashCode(this.image);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final DrawImage other = (DrawImage) obj;
      if (!Objects.equals(this.source, other.source))
        return false;
      if (!Objects.equals(this.dest, other.dest))
        return false;
      if (!Objects.equals(this.image, other.image))
        return false;
      return true;
    }
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
  
  /* ATTRIBUTES */
  private Set<DrawCommand> draw_queue;
  private ICamera camera = null;
  private V2 size = new V2();
  private boolean use_camera = false;

  /* METHODS */
    
  // creation
  /**
   * Create the JPanel.
   */
  public AWTCanvas()
  {
    draw_queue = new LinkedHashSet<DrawCommand>();
  }
  
  /* IMPLEMENTATIONS -- ICANVAS */
  
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
  public synchronized ICanvas setSize(V2 size)
  {
    // reset size
    this.size = size;
    
    // reset camera
    if(use_camera)
      camera.setProjectionSize(size);
    
    return this;
  }
  
  @Override
  public synchronized ICanvas setCamera(ICamera camera)
  {
    // attach camera and reset its field size
    this.camera = camera;
    camera.setProjectionSize(size);
    
    // maintain invariant: (camera == null) => use_camera = false
    if(!use_camera && camera != null)
      use_camera = true;
    else if(camera == null)
      use_camera = false;
    
    return this;
  }

  // modify the paintbrush state
  @Override
  public synchronized ICanvas setColour(Colour colour)
  {
    draw_queue.add(new ColourChange(colour));
    return this;
  }

  @Override
  public synchronized ICanvas setLineWidth(float lineWidth)
  {
    draw_queue.add(new LineWidthChange(lineWidth));
    return this;
  }

  @Override
  public synchronized ICanvas setCanvasFont(Font new_font)
  {
    draw_queue.add(new FontChange(new_font));
    return this;
  }
  
  @Override
  public synchronized ICanvas setFontSize(int size)
  {
    draw_queue.add(new FontSizeChange(size));
    return this;
  }
  
  @Override
  public synchronized ICanvas setCameraActive(boolean use_camera)
  {
    this.use_camera = use_camera;
    return this;
  }

  // drawing functions
  @Override
  public synchronized void clear()
  {
    // empty the list of draw_queue
    draw_queue.clear();
  }
  
  @Override
  public synchronized void circle(V2 centre, float radius, boolean fill)
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
    
    draw_queue.add(new DrawShape(
      new Ellipse2D.Float(pov_centre.x-radius, pov_centre.y-radius, 
                          radius*2, radius*2), fill));
  }

  @Override
  public synchronized void line(V2 start, V2 end)
  {
    // move based on camera position where applicable
    V2 pov_start = (use_camera) ? camera.getPerspective(start) : start;
    V2 pov_end = (use_camera) ? camera.getPerspective(end) : end;
    
    draw_queue.add(new DrawShape(new Line2D.Float(pov_start.x, pov_start.y, 
                                    pov_end.x, pov_end.y), false)); 
                                                          // lines are not filled
  }

  @Override
  public synchronized void box(Rect rect, boolean fill)
  {
    // move based on camera position where applicable
    Rect pov_rect = (use_camera) ? camera.getPerspective(rect).ceil() : rect;
    
    draw_queue.add(new DrawShape(
      new Rectangle2D.Float(pov_rect.x, pov_rect.y, 
                            pov_rect.w, pov_rect.h), fill));
  }

  @Override
  public synchronized void text(String string, V2 position)
  {
    // move based on camera position where applicable
    V2 pov_pos = (use_camera) ? camera.getPerspective(position) : position;
    draw_queue.add(new DrawText(string, pov_pos));
  }
  
  @Override
  public synchronized void texture(ITexture texture, Rect source, Rect destination)
  {
    // fail if wrong kind of texture
    if(!(texture instanceof AWTTexture))
      return;
    AWTTexture awt_texture = (AWTTexture)texture;
    
    draw_queue.add(new DrawImage(source, destination, awt_texture.getImage()));
  }
  
  @Override
  public synchronized void fill()
  {
    draw_queue.add(new DrawShape(
      new Rectangle2D.Float(0, 0, size.x, size.y), true));
  }
  
  @Override
  public void angleBox(V2 o, V2 d, float size, boolean fill)
  {
    // move based on camera position where applicable
    V2 po = (use_camera) ? camera.getPerspective(o) : o;
    // scale direction-vector based on zoom
    V2 pd = (use_camera) ? d.clone().scale(camera.getZoom()*size) 
                          : d.clone().scale(size);

    int xpts[] = {(int)(po.x + pd.x+pd.y), (int)(po.x + pd.x-pd.y), 
                  (int)(po.x + -pd.x-pd.y), (int)(po.x + -pd.x+pd.y)},
        ypts[] = {(int)(po.y + -pd.x+pd.y), (int)(po.y + pd.x+pd.y), 
                  (int)(po.y + pd.x-pd.y), (int)(po.y + -pd.x-pd.y)};
    draw_queue.add(new DrawShape(new Polygon(xpts, ypts, 4), fill));
  }
    
  @Override
  public void triangle(V2 a, V2 b, V2 c, boolean fill)
  {
    V2 pa = (use_camera) ? camera.getPerspective(a) : a,
      pb = (use_camera) ? camera.getPerspective(b) : b,
      pc = (use_camera) ? camera.getPerspective(c) : c;
    
    int xpts[] = {(int)pa.x, (int)pb.x,(int)pc.x},
        ypts[] = {(int)pa.y, (int)pb.y,(int)pc.y};
    draw_queue.add(new DrawShape(new Polygon(xpts, ypts, 3), fill));
  }

  
  
  /* OVERRIDES -- JCOMPONENT */
  
  @Override
  public synchronized void paintComponent(Graphics g)
  {
    int i = 0;
    
    // Get the graphics object
    Graphics2D g2d = (Graphics2D)g;
    
    // Clear the screen in white
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    // Draw each shape in black by default
    g2d.setColor(Color.BLACK);
    for(Object command : draw_queue)
    {
      // draw a shape
      if(command instanceof DrawShape)
      {
        DrawShape shape_cmd = (DrawShape)command; 
        if(shape_cmd.fill)
          g2d.fill(shape_cmd.shape);  // fill
        else
          g2d.draw(shape_cmd.shape);  // outline
      }
      // draw text
      else if(command instanceof DrawText)
      {
        DrawText text_cmd = (DrawText)command;
        g2d.drawString(text_cmd.text, text_cmd.pos.x, text_cmd.pos.y);
      }
      // draw image
      else if(command instanceof DrawImage)
      {
        DrawImage img_cmd = (DrawImage)command;
        try
        {
          BufferedImage subimage = (img_cmd.source != null)
                          ? img_cmd.image.getSubimage(
                                  (int)img_cmd.source.x, (int)img_cmd.source.y, 
                                  (int)img_cmd.source.w, (int)img_cmd.source.h)
                          : img_cmd.image;
          // Draw it on the screen
          
          Rect p_dest = (use_camera) ? camera.getPerspective(img_cmd.dest).ceil() 
                                    : img_cmd.dest;
          g2d.drawImage(subimage, (int)p_dest.x, (int)p_dest.y, 
                                  (int)p_dest.w, (int)p_dest.h, 
                                  null);
        }
        catch (java.awt.image.RasterFormatException e)
        {
          System.out.println("Invalid sub-rect " + img_cmd.source);
        }
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
      // unknown or unsupported command
      else
        System.out.println("Unrecognized command type " + command);
    }
  }
}