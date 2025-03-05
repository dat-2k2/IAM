package datto.example.exception.handler;

import datto.example.exception.UserExistedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(exception = {UserExistedException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "User existed")
    public void handler(){
    }
}
