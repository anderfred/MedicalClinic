package com.anderfred.medical.clinic.util;

import java.util.Random;
import org.apache.commons.lang3.RandomUtils;

public class IdUtils {
  public static long randomLong() {
    return Math.abs(new Random().nextLong());
  }

  public static long randomLongFromTen() {
    return RandomUtils.nextLong(0, 9);
  }

  public static long randomLongShort() {
    return RandomUtils.nextLong(1000, 90000);
  }

  public static int randomInteger() {
    return Math.abs(new Random().nextInt());
  }

  public static Double randomDouble() {
    return Math.abs(new Random().nextDouble());
  }
}
