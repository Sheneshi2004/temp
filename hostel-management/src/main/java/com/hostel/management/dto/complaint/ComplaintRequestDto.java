package com.hostel.management.dto.complaint;

import com.hostel.management.enums.ComplaintPriority;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintRequestDto {

    @NotNull(message = "Resident ID is required")
    private Long residentId;

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private String category;

    private ComplaintPriority priority;
}
