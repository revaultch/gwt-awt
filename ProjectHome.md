# What is GWT-AWT? #
An emulation of Java's AWT, based on OpenJDK.

It also includes a Wrapper for the HTML5-Canvas, to render the Shapes directly on a Context2d Element of GWT.

## It supports ##
  * Geoms like java.awt.Rectangle (java.awt.Rectangle2D)...
  * All calculations inside them (like intersec, contains,...)
  * Print the Shapes directly on a HTML5-Canvas

## It doesn't support now ##
  * hashcode() methods
  * some special methods

## Additional ##
  * HTML5-Canvas wrapper (WebGraphics)

## How to add it to your project ##
Add the following to your pom.xml in dependencies
```
<dependency>
    <groupId>com.levigo</groupId>
    <artifactId>gwt-awt</artifactId>
    <version>1.0.0</version>
</dependency>
```
Add the following to your **.gwt.xml
```
<inherits name="com.levigo.util.GwtAwt" />
```**

## Usage ##
```
// Basic stuff
Canvas canvas = Canvas.createIfSupported();
RootPanel.get().add(canvas);
canvas.setPixelSize(200, 200);
canvas.setCoordinateSpaceHeight(200);
canvas.setCoordinateSpaceWidth(200);

// Create some Shapes
Rectangle r = new Rectangle(10, 10, 30, 30);
Path2D path = new Path2D.Double();
path.moveTo(10, 10);
path.curveTo(10, 0, 40, 0, 40, 10);

// Create Context and WebGraphics
Context2d ctx = canvas.getContext2d();
WebGraphics g = new WebGraphics(ctx);

// Draw it on the Canvas
g.setColor(java.awt.Color.red);
g.draw(r);
g.setColor(java.awt.Color.blue);
g.fill(path);
```

Result:
![http://wiki.gwt-awt.googlecode.com/git/Test.png](http://wiki.gwt-awt.googlecode.com/git/Test.png)


---

License:: GPLv2 with ClasspathException (same as OpenJdk)