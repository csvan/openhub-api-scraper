package database

import database.entity.Organisation
import database.manager.OrganisationDAO

object OrganisationManager extends AbstractManager[Organisation, OrganisationDAO](new OrganisationDAO())
