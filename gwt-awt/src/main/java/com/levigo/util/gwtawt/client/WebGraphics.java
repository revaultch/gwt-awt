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

import java.awt.Color;
import java.awt.Component;
import java.awt.Shape;
import java.awt.geom.PathIterator;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Graphics2D replacement for GWT
 * 
 * Works as a Wrapper around the HTML5 Context2d element
 */
public class WebGraphics {
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

  /**
   * Forces a Repaint of the Component
   */
  public void forceRepaint() {
    if (component != null)
      component.repaint();
  }

  /**
   * @return the HTML5-Canvas-Context2d Element
   */
  public Context2d getContext2d() {
    return context;
  }

  public void translate(int x, int y) {
    context.translate(x, y);
  }

  /**
   * Sets the Color to Fill/Stroke
   * 
   * @param color the new color
   */
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

  /**
   * Strokes a shape
   * 
   * @param shape the shape to stroke
   */
  public void draw(Shape shape) {
    if (shape != null) {
      path(shape);
      context.stroke();
    }
  }

  /**
   * Fills a shape
   * 
   * @param shape the shape to fill
   */
  public void fill(Shape shape) {
    if (shape != null) {
      path(shape);
      context.fill();
    }
  }

  private void path(Shape shape) {
    PathIterator i = shape.getPathIterator(null);
    float[] coords = new float[4];
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
}
