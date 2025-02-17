/*
 * The MIT License
 *
 *   Copyright (c) 2023, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package io.github.krandom.randomizers.registry;

import static io.github.krandom.util.ConversionUtils.convertDateToLocalDate;
import static java.sql.Date.valueOf;

import io.github.krandom.KRandomParameters;
import io.github.krandom.annotation.Priority;
import io.github.krandom.api.Randomizer;
import io.github.krandom.api.RandomizerRegistry;
import io.github.krandom.randomizers.misc.BooleanRandomizer;
import io.github.krandom.randomizers.misc.LocaleRandomizer;
import io.github.krandom.randomizers.misc.SkipRandomizer;
import io.github.krandom.randomizers.misc.UUIDRandomizer;
import io.github.krandom.randomizers.net.UriRandomizer;
import io.github.krandom.randomizers.net.UrlRandomizer;
import io.github.krandom.randomizers.number.*;
import io.github.krandom.randomizers.range.DateRangeRandomizer;
import io.github.krandom.randomizers.range.SqlDateRangeRandomizer;
import io.github.krandom.randomizers.text.CharacterRandomizer;
import io.github.krandom.randomizers.text.StringRandomizer;
import io.github.krandom.randomizers.time.*;
import io.github.krandom.randomizers.time.CalendarRandomizer;
import io.github.krandom.randomizers.time.SqlTimeRandomizer;
import io.github.krandom.randomizers.time.SqlTimestampRandomizer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Registry for Java built-in types.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
@Priority(-4)
public class InternalRandomizerRegistry implements RandomizerRegistry {

  private final Map<Class<?>, Randomizer<?>> randomizers = new HashMap<>();

  @Override
  public void init(KRandomParameters parameters) {
    long seed = parameters.getSeed();
    Charset charset = parameters.getCharset();
    randomizers.put(
        String.class,
        new StringRandomizer(
            charset,
            parameters.getStringLengthRange().getMin(),
            parameters.getStringLengthRange().getMax(),
            seed));
    CharacterRandomizer characterRandomizer = new CharacterRandomizer(charset, seed);
    randomizers.put(Character.class, characterRandomizer);
    randomizers.put(char.class, characterRandomizer);
    randomizers.put(Boolean.class, new BooleanRandomizer(seed));
    randomizers.put(boolean.class, new BooleanRandomizer(seed));
    randomizers.put(Byte.class, new ByteRandomizer(seed));
    randomizers.put(byte.class, new ByteRandomizer(seed));
    randomizers.put(Short.class, new ShortRandomizer(seed));
    randomizers.put(short.class, new ShortRandomizer(seed));
    randomizers.put(Integer.class, new IntegerRandomizer(seed));
    randomizers.put(int.class, new IntegerRandomizer(seed));
    randomizers.put(Long.class, new LongRandomizer(seed));
    randomizers.put(long.class, new LongRandomizer(seed));
    randomizers.put(Double.class, new DoubleRandomizer(seed));
    randomizers.put(double.class, new DoubleRandomizer(seed));
    randomizers.put(Float.class, new FloatRandomizer(seed));
    randomizers.put(float.class, new FloatRandomizer(seed));
    randomizers.put(BigInteger.class, new BigIntegerRandomizer(seed));
    randomizers.put(BigDecimal.class, new BigDecimalRandomizer(seed));
    randomizers.put(AtomicLong.class, new AtomicLongRandomizer(seed));
    randomizers.put(AtomicInteger.class, new AtomicIntegerRandomizer(seed));
    Date minDate = new Date(Long.MIN_VALUE);
    Date maxDate = new Date(Long.MAX_VALUE);
    minDate =
        convertDateToLocalDate(minDate).isAfter(parameters.getDateRange().getMin())
            ? minDate
            : valueOf(parameters.getDateRange().getMin());
    maxDate =
        convertDateToLocalDate(maxDate).isBefore(parameters.getDateRange().getMax())
            ? maxDate
            : valueOf(parameters.getDateRange().getMax());
    randomizers.put(Date.class, new DateRangeRandomizer(minDate, maxDate, seed));
    randomizers.put(
        java.sql.Date.class,
        new SqlDateRangeRandomizer(
            new java.sql.Date(minDate.getTime()), new java.sql.Date(maxDate.getTime()), seed));
    randomizers.put(java.sql.Time.class, new SqlTimeRandomizer(seed));
    randomizers.put(java.sql.Timestamp.class, new SqlTimestampRandomizer(seed));
    randomizers.put(Calendar.class, new CalendarRandomizer(seed));
    randomizers.put(URL.class, new UrlRandomizer(seed));
    randomizers.put(URI.class, new UriRandomizer(seed));
    randomizers.put(Locale.class, new LocaleRandomizer(seed));
    randomizers.put(UUID.class, new UUIDRandomizer(seed));
    // issue #280: skip fields of type Class
    randomizers.put(Class.class, new SkipRandomizer());
    // issue #7: skip fields of type Exception
    randomizers.put(Exception.class, new SkipRandomizer());
  }

  @Override
  public Randomizer<?> getRandomizer(final Field field) {
    return getRandomizer(field.getType());
  }

  /** {@inheritDoc} */
  @Override
  public Randomizer<?> getRandomizer(Class<?> type) {
    return randomizers.get(type);
  }
}
