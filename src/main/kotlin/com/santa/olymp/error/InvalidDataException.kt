package com.santa.olymp.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


class InvalidDataException(message: String): RuntimeException(message)

@ControllerAdvice
class InvalidDataAdvice {

  @ResponseBody
  @ExceptionHandler(InvalidDataException::class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  fun stringHandler(e: InvalidDataException) = e.message
}