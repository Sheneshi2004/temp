package com.hostel.management.repository;

import com.hostel.management.entity.Complaint;
import com.hostel.management.enums.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByResidentId(Long residentId);

    List<Complaint> findByStatus(ComplaintStatus status);

    List<Complaint> findByResidentIdAndStatus(Long residentId, ComplaintStatus status);

    long countByStatus(ComplaintStatus status);
}
