package database

import database.entity.Link
import database.manager.LinkDAO

object LinkManager extends AbstractManager[Link, LinkDAO](new LinkDAO())
