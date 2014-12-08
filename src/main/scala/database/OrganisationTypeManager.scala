package database

import database.entity.OrganisationType
import database.manager.OrganisationTypeDAO

object OrganisationTypeManager extends AbstractManager[OrganisationType, OrganisationTypeDAO](new OrganisationTypeDAO())
