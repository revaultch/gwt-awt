package com.levigo.util.gwtawt.shared;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Path2dFloatSerializable extends Path2D.Float implements IsSerializable {
  private static final long serialVersionUID = 1L;

  @Deprecated
  public Path2dFloatSerializable() {
    // For gwt only
  }

  private ArrayList<java.lang.Float> _values;
  private ArrayList<Integer> _types;

  public Path2dFloatSerializable(Path2D.Float path) {
    _values = new ArrayList<java.lang.Float>();
    _types = new ArrayList<Integer>();

    PathIterator i = path.getPathIterator(null);
    float[] coord = new float[6];
    for (; !i.isDone(); i.next()) {
      int type = i.currentSegment(coord);

      int amount = 0;
      switch (type){
        case PathIterator.SEG_MOVETO :
          amount = 2;
          break;
        case PathIterator.SEG_LINETO :
          amount = 2;
          break;
        case PathIterator.SEG_QUADTO :
          amount = 4;
          break;
        case PathIterator.SEG_CUBICTO :
          amount = 6;
          break;
        case PathIterator.SEG_CLOSE :
          amount = 0;
          break;
      }
      _types.add(type);
      for (int it = 0; it < amount; it++)
        _values.add(coord[it]);
    }
  }

  public void init() {
    assert (_values != null) : "Already inited (or bad serialisation)";
    java.lang.Float[] coords = _values.toArray(new java.lang.Float[_values.size()]);
    int next = 0;
    for (Integer type : _types) {
      switch (type){
        case PathIterator.SEG_MOVETO :
          moveTo(coords[next++], coords[next++]);
          break;
        case PathIterator.SEG_LINETO :
          lineTo(coords[next++], coords[next++]);
          break;
        case PathIterator.SEG_QUADTO :
          quadTo(coords[next++], coords[next++], coords[next++], coords[next++]);
          break;
        case PathIterator.SEG_CUBICTO :
          curveTo(coords[next++], coords[next++], coords[next++], coords[next++], coords[next++], coords[next++]);
          break;
        case PathIterator.SEG_CLOSE :
          closePath();
          break;
      }
    }
    _values = null;
    _types = null;
  }


}
