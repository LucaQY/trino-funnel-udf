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

package io.trino.plugin.scalar;

import io.airlift.slice.Slice;
import io.airlift.slice.SliceUtf8;
import io.airlift.slice.Slices;
import io.trino.plugin.util.JodaTimeUtil;
import io.trino.spi.StandardErrorCode;
import io.trino.spi.TrinoException;
import io.trino.spi.function.Description;
import io.trino.spi.function.ScalarFunction;
import io.trino.spi.function.SqlType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateFunction {

    private DateFunction() {
    }
    private static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date().withZoneUTC();

    @ScalarFunction
    @Description("Gets the string of yesterday's date")
    @SqlType("varchar")
    public static Slice yesterday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, -24);
        String yesterday = format.format(calendar.getTime());
        Slice s = Slices.utf8Slice(yesterday);
        return s;
    }

    @ScalarFunction
    @Description("date 2 str")
    @SqlType("varchar")
    public static Slice date2str(@SqlType("timestamp(3) with time zone") long value) {
        return Slices.utf8Slice(JodaTimeUtil.printTimestampWithoutTimeZone(value >> 12));
    }

    @ScalarFunction
    @Description("str 2 date")
    @SqlType("date")
    public static long str2date(@SqlType("varchar") Slice value) {
        try {
            return TimeUnit.MILLISECONDS.toDays(DATE_FORMATTER.parseMillis(JodaTimeUtil.printTimestampWithoutTimeZone(JodaTimeUtil.parseTimestampWithoutTimeZone(SliceUtf8.trim(value).toStringUtf8()))));
        } catch (IllegalArgumentException var2) {
            throw new TrinoException(StandardErrorCode.INVALID_CAST_ARGUMENT, "Value cannot be cast to date: " + value.toStringUtf8(), var2);
        }
    }
}
