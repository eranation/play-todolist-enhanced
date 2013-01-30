package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import java.sql.Connection

case class Task(id: Long, label: String) 
object Task {
  val task = {
    get[Long]("task.id") ~
    get[String]("task.label") map {
      case id ~ label => Task(id, label)
    }
  }
  
 /* val x = DB.withConnection[List[Task]] (
      (c:Connection)=> {
        SQL("select * from task").as(task *)(c)
      }
  )*/
  
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }
  
  def create(label: String) {
    DB.withConnection { implicit c =>
      SQL("insert into task (label) values ({label})").on(
          'label -> label
      ).executeUpdate()
    }
  }
  
  def update(id:Long, label: String) {
    DB.withConnection { implicit c =>
    SQL("update task set label = {label} where id = {id}").on(
        'label -> label,
        'id -> id
        ).executeUpdate()
    }
  }
  def delete(id: Long){
    DB.withConnection { implicit c =>
      SQL("delete from task where id = {id}").on(
          'id-> id
      ).executeUpdate()
    }
  }
  
  def findById(id: Long):Task = {
      DB.withConnection { implicit c =>
        SQL("select * from task where id = {id}").on('id->id).as(task.single)
        //SQL("select * from task where id = {id}").on('id->id).single(task)
    }
  }
}