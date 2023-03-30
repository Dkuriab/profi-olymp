package com.santa.olymp.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus


class TossNotAvailableException(message: String): RuntimeException(message)

@ControllerAdvice
class TossNotAvailableAdvice {

  @ResponseBody
  @ExceptionHandler(TossNotAvailableException::class)
  @ResponseStatus(HttpStatus.CONFLICT)
  fun stringHandler(e: TossNotAvailableException) = e.message
}