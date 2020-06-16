package com.example

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDB() {
    Database.connect(
        "jdbc:mysql://localhost:3336/ktor?useSSL=false", driver = "com.mysql.jdbc.Driver",
        user = "root", password = "1234"
    )
    transaction {
        SchemaUtils.createMissingTablesAndColumns(Users, Articles, Comments,Images)
    }
}

object Users : IntIdTable() {
    val username = varchar("username", length = 255).uniqueIndex()
    val password = varchar("password", length = 255)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var username by Users.username
    var password by Users.password
}

object Articles : IntIdTable() {
    val title = varchar("title", length = 255)
    val author = varchar("author", length = 255)
    val content = text("content")
    val time = long("time")
    val view = integer("view")
    val comment = integer("comment")
}

class Article(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Article>(Articles)

    var title by Articles.title
    var author by Articles.author
    var content by Articles.content
    var time by Articles.time
    var view by Articles.view
    var comment by Articles.comment
}

object Comments : IntIdTable() {
    val username = varchar("username", 255)
    val content = text("content")
    val articleId = integer("articleId")
    val time = long("time")

}

class Comment(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Comment>(Comments)

    var username by Comments.username
    var content by Comments.content
    var articleId by Comments.articleId
    var time by Comments.time

}

object Images : IntIdTable() {
    val fileName = varchar("fileName", 255)
    val time = long("time")
}

class Image(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Image>(Images)

    var fileName by Images.fileName
    var time by Images.time
}