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
package io.github.krandom.validation;

import static io.github.krandom.KRandomParameters.DEFAULT_DATE_RANGE;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

class PastOrPresentAnnotationHandler extends AbstractTemporalBaseAnnotationHandler {
  PastOrPresentAnnotationHandler() {
    super(
        Date.from(
            LocalDate.now()
                .minusYears(DEFAULT_DATE_RANGE)
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()),
        Date.from(Instant.now()),
        Instant.now().minus(DEFAULT_DATE_RANGE * 365, ChronoUnit.DAYS),
        Instant.now().minusSeconds(DEFAULT_DATE_RANGE),
        LocalDate.now().minusYears(DEFAULT_DATE_RANGE),
        LocalDate.now(),
        LocalDateTime.now().minusSeconds(DEFAULT_DATE_RANGE).minusYears(DEFAULT_DATE_RANGE),
        LocalDateTime.now().minusSeconds(DEFAULT_DATE_RANGE),
        LocalTime.MIN,
        LocalTime.now().minusSeconds(DEFAULT_DATE_RANGE),
        MonthDay.of(1, 1),
        MonthDay.now(),
        OffsetDateTime.now().minusSeconds(10).minusYears(DEFAULT_DATE_RANGE),
        OffsetDateTime.now().minusSeconds(10),
        OffsetTime.MIN,
        OffsetTime.now().minusSeconds(10),
        Year.now().minusYears(DEFAULT_DATE_RANGE),
        Year.now(),
        YearMonth.now().minusYears(DEFAULT_DATE_RANGE),
        YearMonth.now(),
        ZonedDateTime.now().minusSeconds(10).minusYears(DEFAULT_DATE_RANGE),
        ZonedDateTime.now().minusSeconds(10));
  }
}
