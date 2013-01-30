package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Task

object Application extends Controller {

  val taskForm = Form(
    "label" -> nonEmptyText)

  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    //Ok("Hello world")
    Redirect(routes.Application.tasks)
  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }
  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.Application.tasks)
      })
  }
 
  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }
  
  def showTask(id: Long) = Action {
    val task = Task.findById(id)
    Ok(views.html.task(task, taskForm.fill(task.label)))
  }
  
  def editTask(id:Long) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.update(id,label)
        Redirect(routes.Application.tasks)
      })
  }
}