package database

import database.entity.Page
import database.manager.PageDAO

object PageManager extends AbstractManager[Page, PageDAO](new PageDAO())
