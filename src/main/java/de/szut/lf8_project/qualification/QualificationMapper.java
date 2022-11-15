package de.szut.lf8_project.qualification;


import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class QualificationMapper {

    public static QualificationEntity StringToEntity(String skill){
        var entity = new QualificationEntity();
        entity.setSkill(skill);

        return entity;
    }
}
