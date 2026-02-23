package com.hostel.management.service.impl;

import com.hostel.management.dto.complaint.ComplaintRequestDto;
import com.hostel.management.dto.complaint.ComplaintResponseDto;
import com.hostel.management.entity.Complaint;
import com.hostel.management.entity.Resident;
import com.hostel.management.enums.ComplaintPriority;
import com.hostel.management.enums.ComplaintStatus;
import com.hostel.management.exception.ResourceNotFoundException;
import com.hostel.management.repository.ComplaintRepository;
import com.hostel.management.repository.ResidentRepository;
import com.hostel.management.service.ComplaintService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ComplaintService} containing all complaint business logic.
 */
@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ResidentRepository residentRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository,
                                ResidentRepository residentRepository) {
        this.complaintRepository = complaintRepository;
        this.residentRepository = residentRepository;
    }

    @Override
    public ComplaintResponseDto createComplaint(ComplaintRequestDto dto) {
        Resident resident = findResidentById(dto.getResidentId());

        Complaint complaint = Complaint.builder()
                .resident(resident)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .priority(dto.getPriority() != null ? dto.getPriority() : ComplaintPriority.LOW)
                .status(ComplaintStatus.PENDING)
                .complaintDate(LocalDate.now())
                .build();

        return toDto(complaintRepository.save(complaint));
    }

    @Override
    public ComplaintResponseDto updateComplaint(Long id, ComplaintRequestDto dto) {
        Complaint complaint = findById(id);

        if (dto.getTitle() != null) complaint.setTitle(dto.getTitle());
        if (dto.getDescription() != null) complaint.setDescription(dto.getDescription());
        if (dto.getCategory() != null) complaint.setCategory(dto.getCategory());
        if (dto.getPriority() != null) complaint.setPriority(dto.getPriority());

        return toDto(complaintRepository.save(complaint));
    }

    @Override
    public void deleteComplaint(Long id) {
        if (!complaintRepository.existsById(id)) {
            throw new ResourceNotFoundException("Complaint", id);
        }
        complaintRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponseDto getComplaintById(Long id) {
        return toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getAllComplaints(Long residentId, ComplaintStatus status) {
        List<Complaint> complaints;
        if (residentId != null && status != null) {
            complaints = complaintRepository.findByResidentIdAndStatus(residentId, status);
        } else if (residentId != null) {
            complaints = complaintRepository.findByResidentId(residentId);
        } else if (status != null) {
            complaints = complaintRepository.findByStatus(status);
        } else {
            complaints = complaintRepository.findAll();
        }
        return complaints.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public ComplaintResponseDto updateStatus(Long id, ComplaintStatus status, String resolution) {
        Complaint complaint = findById(id);
        complaint.setStatus(status);
        if (resolution != null) complaint.setResolution(resolution);
        if (status == ComplaintStatus.RESOLVED) {
            complaint.setResolvedDate(LocalDate.now());
        }
        return toDto(complaintRepository.save(complaint));
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getComplaintStats() {
        return Map.of(
                "total", complaintRepository.count(),
                "pending", complaintRepository.countByStatus(ComplaintStatus.PENDING),
                "inProgress", complaintRepository.countByStatus(ComplaintStatus.IN_PROGRESS),
                "resolved", complaintRepository.countByStatus(ComplaintStatus.RESOLVED)
        );
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Complaint findById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint", id));
    }

    private Resident findResidentById(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resident", id));
    }

    private ComplaintResponseDto toDto(Complaint c) {
        return ComplaintResponseDto.builder()
                .id(c.getId())
                .residentId(c.getResident().getId())
                .residentName(c.getResident().getName())
                .roomNumber(c.getResident().getRoom() != null ? c.getResident().getRoom().getRoomNumber() : null)
                .title(c.getTitle())
                .description(c.getDescription())
                .category(c.getCategory())
                .priority(c.getPriority())
                .status(c.getStatus())
                .resolution(c.getResolution())
                .complaintDate(c.getComplaintDate())
                .resolvedDate(c.getResolvedDate())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}
