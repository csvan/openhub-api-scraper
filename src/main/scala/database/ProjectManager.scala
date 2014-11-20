package database

import database.entity.Project
import database.manager.ProjectDAO

object ProjectManager extends AbstractManager[Project, ProjectDAO](new ProjectDAO())
