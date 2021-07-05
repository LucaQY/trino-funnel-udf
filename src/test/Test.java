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

import java.util.concurrent.TimeUnit;

import io.trino.plugin.util.JodaTimeUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class Test {

    private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date().withZoneUTC();
    private static DateTime dt = new DateTime();
    public static int parseDate(String value) {
        return (int)TimeUnit.MILLISECONDS.toDays(DATE_FORMATTER.parseMillis(value));
    }

    public static String printDate(long days) {
        return DATE_FORMATTER.print(TimeUnit.DAYS.toMillis(days));
    }

    public static void main(String[] args) {
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27 00:00:01.000 Asia/Hong_Kong")));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27 00:00:00.000")));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27 15:00:00")));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27")));
        System.out.println(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27 00:00:01.000 Asia/Hong_Kong"));
        System.out.println(JodaTimeUtil.parseTimestampWithoutTimeZone(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-10-27 00:00:01.000 Asia/Hong_Kong"))));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(1603756800000L));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(1603814400000L));
        System.out.println(parseDate(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-11-01 16:00:00"))));
        System.out.println(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-11-01 15:00:00"));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-11-01 23:00:00")));
        System.out.println(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-11-01")));
        System.out.println(parseDate(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone("2020-11-01"))));
    }
}
