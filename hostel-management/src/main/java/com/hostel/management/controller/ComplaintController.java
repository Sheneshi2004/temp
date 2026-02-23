package com.hostel.management.controller;

import com.hostel.management.dto.complaint.ComplaintRequestDto;
import com.hostel.management.dto.complaint.ComplaintResponseDto;
import com.hostel.management.enums.ComplaintStatus;
import com.hostel.management.response.ApiResponse;
import com.hostel.management.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ComplaintResponseDto>> createComplaint(
            @Valid @RequestBody ComplaintRequestDto requestDto) {
        ComplaintResponseDto complaint = complaintService.createComplaint(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Complaint submitted successfully.", complaint));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponseDto>> updateComplaint(
            @PathVariable Long id,
            @RequestBody ComplaintRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success("Complaint updated.",
                complaintService.updateComplaint(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok(ApiResponse.success("Complaint deleted."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponseDto>> getComplaintById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Complaint retrieved.",
                complaintService.getComplaintById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ComplaintResponseDto>>> getAllComplaints(
            @RequestParam(required = false) Long residentId,
            @RequestParam(required = false) ComplaintStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Complaints retrieved.",
                complaintService.getAllComplaints(residentId, status)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ComplaintResponseDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam ComplaintStatus status,
            @RequestParam(required = false) String resolution) {
        return ResponseEntity.ok(ApiResponse.success("Complaint status updated.",
                complaintService.updateStatus(id, status, resolution)));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComplaintStats() {
        return ResponseEntity.ok(ApiResponse.success("Complaint stats retrieved.",
                complaintService.getComplaintStats()));
    }
}
