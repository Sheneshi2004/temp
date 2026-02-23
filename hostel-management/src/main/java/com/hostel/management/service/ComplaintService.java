package com.hostel.management.service;

import com.hostel.management.dto.complaint.ComplaintRequestDto;
import com.hostel.management.dto.complaint.ComplaintResponseDto;
import com.hostel.management.enums.ComplaintStatus;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Complaint and Maintenance management operations.
 */
public interface ComplaintService {

    /** Submit a new complaint. */
    ComplaintResponseDto createComplaint(ComplaintRequestDto requestDto);

    /** Update complaint details or status. */
    ComplaintResponseDto updateComplaint(Long id, ComplaintRequestDto requestDto);

    /** Delete a complaint. */
    void deleteComplaint(Long id);

    /** Get a complaint by ID. */
    ComplaintResponseDto getComplaintById(Long id);

    /** Get all complaints with optional residentId or status filters. */
    List<ComplaintResponseDto> getAllComplaints(Long residentId, ComplaintStatus status);

    /** Update only the status of a complaint. */
    ComplaintResponseDto updateStatus(Long id, ComplaintStatus status, String resolution);

    /** Get complaint stats. */
    Map<String, Object> getComplaintStats();
}
