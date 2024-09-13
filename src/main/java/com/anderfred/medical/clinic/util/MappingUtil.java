package com.anderfred.medical.clinic.util;

import static org.slf4j.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;

public final class MappingUtil {

  private static final Logger log = getLogger(MappingUtil.class);

  private MappingUtil() {}

  public static String toJson(ObjectMapper mapper, Object obj) {
    try {
      return mapper.writeValueAsString(obj);
    } catch (IOException e) {
      String msg = "Can't serialize to json";
      log.error(msg, e);
      throw new IllegalStateException(msg);
    }
  }

  public static <T> T fromJson(ObjectMapper mapper, String json, Class<T> castTo) {
    try {
      return mapper.readValue(json, castTo);
    } catch (IOException e) {
      String msg = "Can't deserialize from json";
      log.error(msg, e);
      throw new IllegalStateException(msg);
    }
  }

  @SuppressWarnings("uncheked")
  public static <T> T copy(ObjectMapper mapper, T obj) {
    return (T) fromJson(mapper, toJson(mapper, obj), obj.getClass());
  }

  public static <T> List<T> fromJsonList(ObjectMapper mapper, String json, Class<T> castTo) {
    try {
      return mapper.readValue(
          json, mapper.getTypeFactory().constructCollectionLikeType(List.class, castTo));
    } catch (IOException e) {
      String msg = "Can't deserialize from json";
      log.error(msg, e);
      throw new IllegalStateException(msg);
    }
  }

  public static <T> List<T> copyList(ObjectMapper objectMapper, List<T> lst, Class<T> castTo) {
    return fromJsonList(objectMapper, toJson(objectMapper, lst), castTo);
  }
}
