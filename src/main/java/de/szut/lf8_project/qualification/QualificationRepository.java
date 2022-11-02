package de.szut.lf8_project.qualification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface QualificationRepository extends JpaRepository<QualificationEntity, Long> {
}
