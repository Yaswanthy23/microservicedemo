package com.crm.user.repository;

import com.crm.user.model.ReportAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportAttachmentRepository extends JpaRepository<ReportAttachment, Long> {
    List<ReportAttachment> findByReportId(Long reportId);
}
