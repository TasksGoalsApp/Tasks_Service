package com.example.Task.Service.business.Impl.tasksImpl;

import com.example.Task.Service.domain.TaskScheduleType;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TaskScheduleValidator {

    public void validate(
            TaskScheduleType scheduleType,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (scheduleType == null) {
            throw new IllegalArgumentException("Schedule type is required");
        }

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }

        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("Task cannot start in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        switch (scheduleType) {
            case FIXED_TIME -> validateFixedTime(startDate, endDate, startTime, endTime, today, now);
            case ALL_DAY -> validateAllDay(startDate, endDate, startTime, endTime);
            case WEEK_RANGE -> validateWeekRange(startDate, endDate, startTime, endTime);
            default -> throw new IllegalArgumentException("Unsupported schedule type");
        }
    }

    private void validateFixedTime(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            LocalDate today,
            LocalTime now
    ) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Fixed time task requires start time and end time");
        }

        if (!startDate.equals(endDate)) {
            throw new IllegalArgumentException("Fixed time task must be for one day only");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (startDate.equals(today) && !startTime.isAfter(now)) {
            throw new IllegalArgumentException("Cannot create or update a fixed time task in the past");
        }
    }

    private void validateAllDay(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (startTime != null || endTime != null) {
            throw new IllegalArgumentException("All day task must not contain time");
        }

        if (!startDate.equals(endDate)) {
            throw new IllegalArgumentException("All day task must be exactly one day");
        }
    }

    private void validateWeekRange(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        if (startTime != null || endTime != null) {
            throw new IllegalArgumentException("Week range task must not contain time");
        }

        if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new IllegalArgumentException("Week range task must start on Monday");
        }

        if (!endDate.equals(startDate.plusDays(6))) {
            throw new IllegalArgumentException("Week range task must end on Sunday of the same week");
        }
    }
}