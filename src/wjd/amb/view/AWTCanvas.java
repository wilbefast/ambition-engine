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
    return new Color(c.r, c.b, c.g, c.a);
  }
  
  /* POSSIBLE DRAW COMMANDS TO BE QUEUED */
  private static class DrawText
  {
    public String text;
    public V2 pos;
    public DrawText(String text, V2 pos) { this.text = text; this.pos = pos; }
  }
  private static class ColourChange
  {
    public Colour colour;
    public ColourChange(Colour colour) { this.colour = colour; }
  }
  private static class LineWidthChange
  {
    public float lineWidth;
    public LineWidthChange(float lineWidth) { this.lineWidth = lineWidth; }
  }
  private static class FontChange
  {
    public Object font;
    public FontChange(Object font) { this.font = font; }
  }
  
  /* ATTRIBUTES */
  private Queue<Object> draw_queue;

  /* METHODS */
  // creation
  /**
   * Create the JPanel.
   */
  public AWTCanvas()
  {
    draw_queue = new LinkedList<Object>();
  }

  // setup functions
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
  public ICanvas setFont(Object new_font)
  {
    draw_queue.add(new FontChange(new_font));
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
    draw_queue.add(new Ellipse2D.Float(centre.x(), centre.y(), radius*2, radius*2));
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
    draw_queue.add(new Line2D.Float(start.x(), start.y(), end.x(), end.y()));
  }

  /**
   * Draw the outline of a Rectangle.
   *
   * @param rect the rectangle object whose outline will be drawn.
   */
  @Override
  public void box(Rect rect)
  {
    draw_queue.add(new Rectangle2D.Float(rect.x(), rect.y(), rect.w(), rect.h()));
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
    draw_queue.add(new DrawText(string, position));
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
      if(command instanceof Shape)
        g2d.fill((Shape)command);
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
      {
        Object new_font = ((FontChange)command).font;
        if(new_font instanceof Font)
          g2d.setFont((Font)(new_font));
        else
          System.out.println("Incorrect font type " + new_font);
      }
      // unknown or unsupported command
      else
        System.out.println("Unrecognized command type " + command);
    }
  }
}