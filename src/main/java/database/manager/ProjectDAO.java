package database.manager;

import database.entity.Project;

/**
 * Created by csvanefalk on 18/11/14.
 */
public class ProjectDAO extends AbstractDAO<Project> {
    public ProjectDAO() {
        super(Project.class);
    }
}
