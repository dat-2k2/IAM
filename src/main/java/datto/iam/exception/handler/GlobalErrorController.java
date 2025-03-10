package datto.iam.exception.handler;

import datto.iam.exception.UserExistedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice(basePackages = {"datto.iam.controller"})
public class GlobalErrorController extends ResponseEntityExceptionHandler {
//    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "User existed")
    @ExceptionHandler(exception = { UserExistedException.class })
    public ResponseEntity<Object> handler(){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User existed");
    }
}
