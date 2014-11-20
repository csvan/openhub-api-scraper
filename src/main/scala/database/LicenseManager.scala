package database

import database.entity.License
import database.manager.LicenseDAO

object LicenseManager extends AbstractManager[License, LicenseDAO](new LicenseDAO())
