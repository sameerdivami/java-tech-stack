package com.familyleague.repository;

import com.familyleague.entity.EmailLog;
import com.familyleague.enums.EmailStatus;
import com.familyleague.enums.EmailType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {
    Page<EmailLog> findAllByDeletedFalse(Pageable pageable);
    Page<EmailLog> findAllByToAddressAndDeletedFalse(String toAddress, Pageable pageable);
    Page<EmailLog> findAllByTypeAndDeletedFalse(EmailType type, Pageable pageable);
    Page<EmailLog> findAllByStatusAndDeletedFalse(EmailStatus status, Pageable pageable);
}
