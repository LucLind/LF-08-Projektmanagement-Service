package de.szut.lf8_project.qualification;

import org.springframework.stereotype.Service;

@Service
public class QualificationService {
    private QualificationRepository repository;
    private QualificationMapper qualificationMapper;

    public QualificationService(QualificationRepository repository, QualificationMapper qualificationMapper){
        this.repository = repository;
        this.qualificationMapper = qualificationMapper;
    }


}
