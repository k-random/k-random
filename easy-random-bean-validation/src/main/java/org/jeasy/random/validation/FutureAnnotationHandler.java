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
package org.jeasy.random.validation;

import static org.jeasy.random.EasyRandomParameters.DEFAULT_DATE_RANGE;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

class FutureAnnotationHandler extends AbstractTemporalBaseAnnotationHandler {
  FutureAnnotationHandler() {
    super(
        Date.from(Instant.now().plusSeconds(86400L)),
        new Date(
            Year.now().plusYears(DEFAULT_DATE_RANGE).getValue(),
            MonthDay.now().getMonthValue(),
            MonthDay.now().getDayOfMonth()),
        Instant.now().plusSeconds(86400L),
        Instant.now().plusSeconds(86400L).plus(DEFAULT_DATE_RANGE * 365, ChronoUnit.DAYS),
        LocalDate.now().plusDays(1L),
        LocalDate.now().plusDays(1L).plusYears(DEFAULT_DATE_RANGE),
        LocalDateTime.now().plusDays(1L),
        LocalDateTime.now().plusDays(1L).plusYears(DEFAULT_DATE_RANGE),
        LocalTime.now().plusSeconds(10L),
        LocalTime.MAX,
        MonthDay.from(MonthDay.now().atYear(LocalDate.now().getYear()).plusDays(1)),
        MonthDay.of(12, 31),
        OffsetDateTime.now().plusDays(1L),
        OffsetDateTime.now().plusDays(1L).plusYears(DEFAULT_DATE_RANGE),
        OffsetTime.now().plusSeconds(10L),
        OffsetTime.MAX,
        Year.now().plusYears(1L),
        Year.now().plusYears(DEFAULT_DATE_RANGE + 1),
        YearMonth.now().plusMonths(1L),
        YearMonth.now().plusMonths(1L).plusYears(DEFAULT_DATE_RANGE),
        ZonedDateTime.now().plusDays(1L),
        ZonedDateTime.now().plusDays(1L).plusYears(DEFAULT_DATE_RANGE));
  }
}
