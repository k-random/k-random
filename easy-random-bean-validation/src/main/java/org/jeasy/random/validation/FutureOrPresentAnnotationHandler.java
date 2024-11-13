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

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.jeasy.random.EasyRandomParameters.DEFAULT_DATE_RANGE;

class FutureOrPresentAnnotationHandler extends AbstractTemporalBaseAnnotationHandler {
  public FutureOrPresentAnnotationHandler() {
    super(
        Date.from(Instant.now()),
        new Date(
            Year.now().plusYears(DEFAULT_DATE_RANGE).getValue(),
            MonthDay.now().getMonthValue(),
            MonthDay.now().getDayOfMonth()),
        Instant.now().plusSeconds(DEFAULT_DATE_RANGE),
        Instant.now().plus(DEFAULT_DATE_RANGE * 365, ChronoUnit.DAYS),
        LocalDate.now(),
        LocalDate.now().plusYears(DEFAULT_DATE_RANGE),
        LocalDateTime.now().plusSeconds(DEFAULT_DATE_RANGE),
        LocalDateTime.now().plusSeconds(DEFAULT_DATE_RANGE).plusYears(DEFAULT_DATE_RANGE),
        LocalTime.now().plusSeconds(DEFAULT_DATE_RANGE),
        LocalTime.MAX,
        MonthDay.now(),
        MonthDay.of(12, 31),
        OffsetDateTime.now().plusSeconds(10),
        OffsetDateTime.now().plusSeconds(10).plusYears(DEFAULT_DATE_RANGE),
        OffsetTime.now().plusSeconds(10),
        OffsetTime.MAX,
        Year.now(),
        Year.now().plusYears(DEFAULT_DATE_RANGE),
        YearMonth.now(),
        YearMonth.now().plusYears(DEFAULT_DATE_RANGE),
        ZonedDateTime.now().plusSeconds(10),
        ZonedDateTime.now().plusSeconds(10).plusYears(DEFAULT_DATE_RANGE));
  }
}
