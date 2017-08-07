package pl.jony.transactions.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.Test;

public class TimestampUtil {
  public static long getCurrentTimestamp() {
    return currentZonedDateTime().toInstant().toEpochMilli();
  }

  public static ZonedDateTime currentZonedDateTime() {
    return ZonedDateTime.now(ZoneId.of("UTC"));
  }

  public static long getOutdatedTimestamp() {
    return ZonedDateTime.now().minusDays(1).toInstant().toEpochMilli();
  }

  @Test
  public void test() {
    System.out.println(getCurrentTimestamp());
  }
}
