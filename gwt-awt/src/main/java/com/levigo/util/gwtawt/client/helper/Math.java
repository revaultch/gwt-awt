package com.levigo.util.gwtawt.client.helper;

public class Math {

  public static double ulp(double d) {
    // TODO correct for our default cases?
    if (d < 0)
      return -d;
    return 0;
  }

}
