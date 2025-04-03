package com.vazant.logix.datagen.utils;

import java.util.List;
import java.util.Random;

public class GeneratorUtils {

  private static final Random random = new Random();

  private GeneratorUtils() {}

  public static <T> T getRandomElement(List<T> list) {
    if (list == null || list.isEmpty()) {
      throw new IllegalArgumentException("List must not be null or empty");
    }
    return list.get(random.nextInt(list.size()));
  }

  public static <T> T getRandomElementAndRemove(List<T> list) {
    if (list == null || list.isEmpty()) {
      throw new IllegalArgumentException("List must not be null or empty");
    }
    int index = random.nextInt(list.size());
    T t = list.get(index);
    list.remove(index);
    return t;
  }

  public static <T> List<T> getRandomElements(List<T> list, int numberOfElements) {
    return random
        .ints(0, list.size())
        .distinct()
        .limit(numberOfElements)
        .mapToObj(list::get)
        .toList();
  }

  public static <T> List<T> getRandomElementsAndRemove(List<T> list, int numberOfElements) {
    List<T> randomList =
        random.ints(0, list.size()).distinct().limit(numberOfElements).mapToObj(list::get).toList();
    if (list.size() < numberOfElements) {
      throw new IllegalArgumentException("Not enough elements in list");
    }
    list.removeAll(randomList);
    return randomList;
  }

  public static String generateUserEmailAddress() {
    return String.format("user%d@example.com", random.nextInt(1000));
  }

  public static String generateInfoEmailAddress() {
    return String.format("info%d@example.com", random.nextInt(1000));
  }

  public static String generatePhoneNumber() {
    return String.format("+43 660 %06d", random.nextInt(1000000));
  }
}
