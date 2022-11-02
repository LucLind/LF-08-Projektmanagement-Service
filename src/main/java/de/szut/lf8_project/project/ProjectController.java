package de.szut.lf8_project.project;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("project")
@RestController
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService service, ProjectMapper projectMapper){
        this.service = service;
        this.projectMapper = projectMapper;
    }

}
