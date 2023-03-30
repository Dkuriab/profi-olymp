package com.santa.olymp.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

class IllegalOperationException(message: String): RuntimeException(message)

@ControllerAdvice
class IllegalOperationAdvice {

  @ResponseBody
  @ExceptionHandler(ElementNotFoundException::class)
  @ResponseStatus(HttpStatus.NOT_MODIFIED)
  fun stringHandler(e: ElementNotFoundException) = e.message
}