package linker.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class LinkServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = Logger.getLogger(LinkServiceExceptionHandler.class.getName());

    LinkServiceExceptionHandler() {
        try {
            FileHandler handler = new FileHandler(String.format(".%sLOG_FILE.txt", File.separator));
            handler.setFormatter(new SimpleFormatter());

            log.addHandler(handler);
            log.setUseParentHandlers(false);
            log.info("Ready to log!");

        } catch (IOException e) {
            System.err.println("Unable to open log file");
            e.printStackTrace();
        }
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLinkNotFoundException(LinkNotFoundException e) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), NOT_FOUND.value(), e.getMessage());
        log.log(Level.INFO, e.getMessage());

        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), INTERNAL_SERVER_ERROR.value(), "Unable to redirect to the external resource");
        log.log(Level.WARNING, "IO EXCEPTION: ", e);

        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNPE(NullPointerException e) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), BAD_REQUEST.value(), "Link parameter is empty!");
        log.log(Level.INFO, "NPE: ", e);

        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(Exception e) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), INTERNAL_SERVER_ERROR.value(), "Oops! Something went wrong!");
        log.log(Level.SEVERE, "UNEXPECTED EXCEPTION: ", e);

        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
