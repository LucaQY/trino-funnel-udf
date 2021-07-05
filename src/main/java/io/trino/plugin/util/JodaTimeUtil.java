// CHECKSTYLE:OFF
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.trino.plugin.util;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class JodaTimeUtil {
    private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date().withZoneUTC();
    private static final DateTimeFormatter TIMESTAMP_WITHOUT_TIME_ZONE_FORMATTER;
    private static final DateTimeFormatter TIMESTAMP_WITH_TIME_ZONE_FORMATTER;
    private static final DateTimeFormatter TIMESTAMP_WITH_OR_WITHOUT_TIME_ZONE_FORMATTER;
    private static final MethodHandle getLocalMillis;

    private JodaTimeUtil() {
    }


    public static int parseDate(String value) {
        return (int) TimeUnit.MILLISECONDS.toDays(DATE_FORMATTER.parseMillis(value));
    }

    public static String printDate(int days) {
        return DATE_FORMATTER.print(TimeUnit.DAYS.toMillis((long)days));
    }

    public static long parseTimestampWithoutTimeZone(String value) {
        LocalDateTime localDateTime = TIMESTAMP_WITH_OR_WITHOUT_TIME_ZONE_FORMATTER.parseLocalDateTime(value);

        try {
            return (long) getLocalMillis.invokeExact(localDateTime.minusHours(8));
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String printTimestampWithoutTimeZone(long timestamp) {
        return TIMESTAMP_WITHOUT_TIME_ZONE_FORMATTER.print(timestamp);
    }

    public static boolean timestampHasTimeZone(String value) {
        try {
            try {
                TIMESTAMP_WITH_TIME_ZONE_FORMATTER.parseMillis(value);
                return true;
            } catch (RuntimeException var2) {
                TIMESTAMP_WITHOUT_TIME_ZONE_FORMATTER.withZoneUTC().parseMillis(value);
                return false;
            }
        } catch (RuntimeException var3) {
            throw new IllegalArgumentException(String.format("Invalid timestamp '%s'", value));
        }
    }

    static {
        DateTimeParser[] timestampWithoutTimeZoneParser = new DateTimeParser[]{DateTimeFormat.forPattern("yyyy-M-d").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSS").getParser()};
        DateTimePrinter timestampWithoutTimeZonePrinter = DateTimeFormat.forPattern("yyyy-MM-dd").getPrinter();
        TIMESTAMP_WITHOUT_TIME_ZONE_FORMATTER = (new DateTimeFormatterBuilder()).append(timestampWithoutTimeZonePrinter, timestampWithoutTimeZoneParser).toFormatter().withOffsetParsed();
        DateTimeParser[] timestampWithTimeZoneParser = new DateTimeParser[]{DateTimeFormat.forPattern("yyyy-M-dZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d Z").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:mZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m Z").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:sZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s Z").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSSZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSS Z").getParser(), DateTimeFormat.forPattern("yyyy-M-dZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d ZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:mZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m ZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:sZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s ZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSSZZZ").getParser(), DateTimeFormat.forPattern("yyyy-M-d H:m:s.SSS ZZZ").getParser()};
        DateTimePrinter timestampWithTimeZonePrinter = DateTimeFormat.forPattern("yyyy-MM-dd").getPrinter();
        TIMESTAMP_WITH_TIME_ZONE_FORMATTER = (new DateTimeFormatterBuilder()).append(timestampWithTimeZonePrinter, timestampWithTimeZoneParser).toFormatter().withOffsetParsed();
        DateTimeParser[] timestampWithOrWithoutTimeZoneParser = new DateTimeParser[timestampWithoutTimeZoneParser.length + timestampWithTimeZoneParser.length];
        System.arraycopy(timestampWithoutTimeZoneParser, 0, timestampWithOrWithoutTimeZoneParser, 0, timestampWithoutTimeZoneParser.length);
        System.arraycopy(timestampWithTimeZoneParser, 0, timestampWithOrWithoutTimeZoneParser, timestampWithoutTimeZoneParser.length, timestampWithTimeZoneParser.length);
        TIMESTAMP_WITH_OR_WITHOUT_TIME_ZONE_FORMATTER = (new DateTimeFormatterBuilder()).append(timestampWithTimeZonePrinter, timestampWithOrWithoutTimeZoneParser).toFormatter().withOffsetParsed();

        try {
            Method getLocalMillisMethod = LocalDateTime.class.getDeclaredMethod("getLocalMillis");
            getLocalMillisMethod.setAccessible(true);
            getLocalMillis = MethodHandles.lookup().unreflect(getLocalMillisMethod);
        } catch (ReflectiveOperationException var5) {
            throw new RuntimeException(var5);
        }
    }
}

