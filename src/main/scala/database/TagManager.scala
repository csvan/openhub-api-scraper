package database

import database.entity.Tag
import database.manager.TagDAO

object TagManager extends AbstractManager[Tag, TagDAO](new TagDAO())
