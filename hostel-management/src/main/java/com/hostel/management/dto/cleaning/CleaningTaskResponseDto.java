package com.hostel.management.dto.cleaning;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleaningTaskResponseDto {

    private Long id;
    private String area;
    private String dayOfWeek;
    private String timeSlot;
    private String assignedStaff;
    private String notes;
    private String completionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
