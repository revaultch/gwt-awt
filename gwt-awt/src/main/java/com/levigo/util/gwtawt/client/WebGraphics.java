/**
 * Copyright (c) 2007-2012, levigo holding gmbh
 * 
 * Unless you have a written license agreement with the copyright holder, the following license
 * terms apply:
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package com.levigo.util.gwtawt.client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.PathIterator;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;

/**
 * Graphics2D replacement for GWT
 * 
 * Works as a Wrapper around the HTML5 Context2d element
 */
public class WebGraphics implements Graphics {
  private final Context2d context;
  private final Component component;

  public WebGraphics(Context2d context) {
    this.context = context;
    this.component = null;
  }

  public WebGraphics(Context2d context, Component component) {
    this.context = context;
    this.component = component;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#forceRepaint()
   */
  @Override
  public void forceRepaint() {
    if (component != null)
      component.repaint();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#getContext2d()
   */
  @Override
  public Context2d getContext2d() {
    return context;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#translate(int, int)
   */
  @Override
  public void translate(int x, int y) {
    context.translate(x, y);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#setColor(java.awt.Color)
   */
  @Override
  public void setColor(Color color) {
    if (color != null) {
      // String colorString="#"+r+g+b;
      String colorString = "rgba(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ", "
          + color.getAlpha() / 255f + ")";

      context.setFillStyle(colorString);
      context.setStrokeStyle(colorString);
    } else {
      System.out.println("Ignoring null-Color");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#setStroke(java.awt.BasicStroke)
   */
  @Override
  public void setStroke(BasicStroke stroke) {
    // TODO support for stroke pattern as soon ie is support stroke patterns
    if (stroke.getDashArray() != null) {
      System.err.println("stroke patterns aren't suppoted");
      throw new IllegalArgumentException("Stroke patterns aren't supported");
    }

    context.setMiterLimit(stroke.getMiterLimit());
    context.setLineWidth(stroke.getLineWidth());

    switch (stroke.getLineJoin()){
      case BasicStroke.JOIN_BEVEL :
        context.setLineJoin(LineJoin.BEVEL);
        break;
      case BasicStroke.JOIN_MITER :
        context.setLineJoin(LineJoin.MITER);
        break;
      case BasicStroke.JOIN_ROUND :
        context.setLineJoin(LineJoin.ROUND);
        break;
      default :
        System.err.println("unknown line join type");
        throw new IllegalArgumentException("illegal line join value");
    }

    switch (stroke.getEndCap()){
      case BasicStroke.CAP_BUTT :
        context.setLineCap(LineCap.BUTT);
        break;
      case BasicStroke.CAP_ROUND :
        context.setLineCap(LineCap.ROUND);
        break;
      case BasicStroke.CAP_SQUARE :
        context.setLineCap(LineCap.SQUARE);
        break;
      default :
        System.err.println("unknown line cap type");
        throw new IllegalArgumentException("illegal line cap value");
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#draw(java.awt.Shape)
   */
  @Override
  public void draw(Shape shape) {
    if (shape != null) {
      path(shape);
      context.stroke();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#fill(java.awt.Shape)
   */
  @Override
  public void fill(Shape shape) {
    if (shape != null) {
      path(shape);
      context.fill();
    }
  }

  private void path(Shape shape) {
    PathIterator i = shape.getPathIterator(null);
    float[] coords = new float[6];
    context.beginPath();
    while (!i.isDone()) {
      int segment = i.currentSegment(coords);
      if (segment == PathIterator.SEG_MOVETO) {
        context.moveTo(coords[0], coords[1]);
      } else if (segment == PathIterator.SEG_LINETO) {
        context.lineTo(coords[0], coords[1]);
      } else if (segment == PathIterator.SEG_QUADTO) {
        context.quadraticCurveTo(coords[0], coords[1], coords[2], coords[3]);
      } else if (segment == PathIterator.SEG_CUBICTO) {
        context.bezierCurveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
      } else if (segment == PathIterator.SEG_CLOSE) {
        context.closePath();
      } else {
        throw new RuntimeException("Unknown Segment " + segment);
      }
      i.next();
    }
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#setFont(java.awt.Font)
   */
  @Override
  public void setFont(Font font) {
     if (font != null && font.getFontName() != null)
     context.setFont(font.getFontName());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#clipRect(int, int, int, int)
   */
  @Override
  public void clipRect(int x, int y, int width, int height) {
    // TODO validate, intersec, null
    this.setClip(x, y, width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#setClip(int, int, int, int)
   */
  @Override
  public void setClip(int x, int y, int width, int height) {
    context.beginPath();
    context.moveTo(x, y);
    context.lineTo(x + width, y);
    context.lineTo(x + width, y + height);
    context.lineTo(x, y + height);
    context.lineTo(x, y);
    context.closePath();
    context.clip();

  }


  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#setClip(java.awt.Shape)
   */
  @Override
  public void setClip(Shape clip) {
    path(clip);
    context.clip();
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#clearRect(int, int, int, int)
   */
  @Override
  public void clearRect(int x, int y, int width, int height) {
    context.clearRect(x, y, width, height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#drawString(java.lang.String, int, int)
   */
  @Override
  public void drawString(String str, int x, int y) {
    context.strokeText(str, x, y);
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.levigo.util.gwtawt.client.WebGraphics#drawChars(char[], int, int, int, int)
   */
  @Override
  public void drawChars(char data[], int offset, int length, int x, int y) {
    drawString(new String(data, offset, length), x, y);
  }
}
