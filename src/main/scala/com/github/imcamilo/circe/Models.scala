package com.github.imcamilo.circe

import java.util.UUID

case class Author(name: String, bio: Option[String])
case class Article(id: UUID, title: String, content: String, author: Author)

