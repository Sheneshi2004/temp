package com.hostel.management.service.impl;

import com.hostel.management.dto.cleaning.CleaningTaskRequestDto;
import com.hostel.management.dto.cleaning.CleaningTaskResponseDto;
import com.hostel.management.entity.CleaningTask;
import com.hostel.management.exception.ResourceNotFoundException;
import com.hostel.management.repository.CleaningTaskRepository;
import com.hostel.management.service.CleaningTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CleaningTaskService} containing all cleaning-task business logic.
 */
@Service
@Transactional
public class CleaningTaskServiceImpl implements CleaningTaskService {

    private final CleaningTaskRepository cleaningTaskRepository;

    public CleaningTaskServiceImpl(CleaningTaskRepository cleaningTaskRepository) {
        this.cleaningTaskRepository = cleaningTaskRepository;
    }

    @Override
    public CleaningTaskResponseDto createTask(CleaningTaskRequestDto dto) {
        CleaningTask task = CleaningTask.builder()
                .area(dto.getArea())
                .dayOfWeek(dto.getDayOfWeek())
                .timeSlot(dto.getTimeSlot())
                .assignedStaff(dto.getAssignedStaff())
                .notes(dto.getNotes())
                .completionStatus(dto.getCompletionStatus() != null ? dto.getCompletionStatus() : "Pending")
                .build();
        return toDto(cleaningTaskRepository.save(task));
    }

    @Override
    public CleaningTaskResponseDto updateTask(Long id, CleaningTaskRequestDto dto) {
        CleaningTask task = findById(id);
        if (dto.getArea() != null) task.setArea(dto.getArea());
        if (dto.getDayOfWeek() != null) task.setDayOfWeek(dto.getDayOfWeek());
        if (dto.getTimeSlot() != null) task.setTimeSlot(dto.getTimeSlot());
        if (dto.getAssignedStaff() != null) task.setAssignedStaff(dto.getAssignedStaff());
        if (dto.getNotes() != null) task.setNotes(dto.getNotes());
        if (dto.getCompletionStatus() != null) task.setCompletionStatus(dto.getCompletionStatus());
        return toDto(cleaningTaskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        if (!cleaningTaskRepository.existsById(id)) {
            throw new ResourceNotFoundException("CleaningTask", id);
        }
        cleaningTaskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CleaningTaskResponseDto getTaskById(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CleaningTaskResponseDto> getAllTasks(String dayOfWeek) {
        List<CleaningTask> tasks = (dayOfWeek != null && !dayOfWeek.isBlank())
                ? cleaningTaskRepository.findByDayOfWeek(dayOfWeek)
                : cleaningTaskRepository.findAll();
        return tasks.stream().map(this::toDto).collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private CleaningTask findById(Long id) {
        return cleaningTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CleaningTask", id));
    }

    private CleaningTaskResponseDto toDto(CleaningTask t) {
        return CleaningTaskResponseDto.builder()
                .id(t.getId())
                .area(t.getArea())
                .dayOfWeek(t.getDayOfWeek())
                .timeSlot(t.getTimeSlot())
                .assignedStaff(t.getAssignedStaff())
                .notes(t.getNotes())
                .completionStatus(t.getCompletionStatus())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}
