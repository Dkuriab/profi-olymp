package com.santa.olymp.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

class GroupNotFoundException(message: String?): RuntimeException(message)

@ControllerAdvice
class GroupNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(GroupNotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  fun stringHandler(e: GroupNotFoundException) = e.message
}