package com.santa.olymp.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

class ElementNotFoundException(id: Long, name: String = "Group"): RuntimeException("$name with passed ID: $id not found")

@ControllerAdvice
class GroupNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ElementNotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun stringHandler(e: ElementNotFoundException) = e.message
}