package com.example.Task.Service.business;
import com.example.Task.Service.business.Impl.tasksImpl.TaskScheduleValidator;
import com.example.Task.Service.domain.TaskScheduleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class TaskScheduleValidatorTest {
    private TaskScheduleValidator validator;

    private final LocalDate today = LocalDate.of(2026, 7, 27);
    private final LocalTime currentTime = LocalTime.of(10, 0);

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-07-27T10:00:00Z"),
                ZoneOffset.UTC
        );

        validator = new TaskScheduleValidator(fixedClock);
    }

    @Test
    void validate_shouldAcceptValidFixedTimeTask() {
        assertDoesNotThrow(() -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0)
                )
        );
    }

    @Test
    void validate_shouldRejectFixedTimeTaskWithoutStartTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        null,
                        LocalTime.of(12, 0)
                )
        );

        assertEquals("Fixed time task requires start time and end time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeTaskWithoutEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        LocalTime.of(11, 0),
                        null
                )
        );

        assertEquals("Fixed time task requires start time and end time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeTaskAcrossMultipleDays() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today.plusDays(1),
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0)
                )
        );

        assertEquals("Fixed time task must be for one day only", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeWhenStartTimeEqualsEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        LocalTime.of(11, 0),
                        LocalTime.of(11, 0)
                )
        );

        assertEquals("Start time must be before end time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeWhenStartTimeIsAfterEndTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        LocalTime.of(13, 0),
                        LocalTime.of(12, 0)
                )
        );

        assertEquals("Start time must be before end time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeTaskInPastOnCurrentDay() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        currentTime.minusMinutes(1),
                        currentTime.plusHours(1)
                )
        );

        assertEquals("Cannot create or update a fixed time task in the past", exception.getMessage());
    }

    @Test
    void validate_shouldRejectFixedTimeTaskStartingAtCurrentTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.FIXED_TIME,
                        today,
                        today,
                        currentTime,
                        currentTime.plusHours(1)
                )
        );

        assertEquals("Cannot create or update a fixed time task in the past", exception.getMessage());
    }

    @Test
    void validate_shouldAcceptValidAllDayTask() {
        assertDoesNotThrow(() -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today,
                        today,
                        null,
                        null
                )
        );
    }

    @Test
    void validate_shouldRejectAllDayTaskWithTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today,
                        today,
                        LocalTime.of(11, 0),
                        null
                )
        );

        assertEquals("All day task must not contain time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectAllDayTaskAcrossMultipleDays() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today,
                        today.plusDays(1),
                        null,
                        null
                )
        );

        assertEquals("All day task must be exactly one day", exception.getMessage());
    }

    @Test
    void validate_shouldAcceptValidWeekRangeTask() {
        LocalDate monday = LocalDate.of(2026, 7, 27);
        LocalDate sunday = monday.plusDays(6);

        assertDoesNotThrow(() -> validator.validate(
                        TaskScheduleType.WEEK_RANGE,
                        monday,
                        sunday,
                        null,
                        null
                )
        );
    }

    @Test
    void validate_shouldRejectWeekRangeTaskWithTime() {
        LocalDate monday = LocalDate.of(2026, 7, 27);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.WEEK_RANGE,
                        monday,
                        monday.plusDays(6),
                        LocalTime.of(11, 0),
                        null
                )
        );

        assertEquals("Week range task must not contain time", exception.getMessage());
    }

    @Test
    void validate_shouldRejectWeekRangeTaskNotStartingOnMonday() {
        LocalDate tuesday = LocalDate.of(2026, 7, 28);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.WEEK_RANGE,
                        tuesday,
                        tuesday.plusDays(6),
                        null,
                        null
                )
        );

        assertEquals("Week range task must start on Monday", exception.getMessage());
    }

    @Test
    void validate_shouldRejectWeekRangeTaskNotEndingOnSunday() {
        LocalDate monday = LocalDate.of(2026, 7, 27);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.WEEK_RANGE,
                        monday,
                        monday.plusDays(5),
                        null,
                        null
                )
        );

        assertEquals("Week range task must end on Sunday of the same week", exception.getMessage());
    }

    @Test
    void validate_shouldRejectTaskWithNullScheduleType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        null,
                        today,
                        today,
                        null,
                        null
                )
        );

        assertEquals("Schedule type is required", exception.getMessage());
    }

    @Test
    void validate_shouldRejectTaskWithNullStartDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        null,
                        today,
                        null,
                        null
                )
        );

        assertEquals("Start date and end date cannot be null", exception.getMessage());
    }

    @Test
    void validate_shouldRejectTaskWithNullEndDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today,
                        null,
                        null,
                        null
                )
        );

        assertEquals("Start date and end date cannot be null", exception.getMessage());
    }

    @Test
    void validate_shouldRejectTaskStartingInPast() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today.minusDays(1),
                        today.minusDays(1),
                        null,
                        null
                )
        );

        assertEquals("Task cannot start in the past", exception.getMessage());
    }

    @Test
    void validate_shouldRejectTaskWhenEndDateIsBeforeStartDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(
                        TaskScheduleType.ALL_DAY,
                        today.plusDays(2),
                        today.plusDays(1),
                        null,
                        null
                )
        );

        assertEquals("End date cannot be before start date", exception.getMessage());
    }

}
