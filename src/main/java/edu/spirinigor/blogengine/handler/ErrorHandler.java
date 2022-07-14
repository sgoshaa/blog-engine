package edu.spirinigor.blogengine.handler;

import edu.spirinigor.blogengine.dto.ErrorImageDto;
import edu.spirinigor.blogengine.dto.ExceptionDto;
import edu.spirinigor.blogengine.dto.ImageDto;
import edu.spirinigor.blogengine.exception.AnyException;
import edu.spirinigor.blogengine.exception.ImageException;
import edu.spirinigor.blogengine.exception.StatisticsNotPublicException;
import edu.spirinigor.blogengine.exception.UserRegistrationException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String size;

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ImageDto> handlerFileSizeLimitExceededException() {
        ErrorImageDto errorImageDto = new ErrorImageDto();
        errorImageDto.setImage("Размер изображения превышает " + size + ".");
        ImageDto imageDto = new ImageDto();
        imageDto.setResult(false);
        imageDto.setErrors(errorImageDto);
        return new ResponseEntity<>(imageDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ImageDto> handlerImageException(ImageException imageException) {
        ErrorImageDto errorImageDto = new ErrorImageDto();
        errorImageDto.setImage(imageException.getMessage());
        ImageDto imageDto = new ImageDto();
        imageDto.setResult(false);
        imageDto.setErrors(errorImageDto);
        return new ResponseEntity<>(imageDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AnyException.class)
    public ResponseEntity<ExceptionDto> handlerImageException(AnyException exception) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ExceptionDto> handlerUserRegistrationException(UserRegistrationException exception) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StatisticsNotPublicException.class)
    public ResponseEntity<ExceptionDto> handlerStatisticsNotPublicException(StatisticsNotPublicException exception) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(exception.getMessage());
        return new ResponseEntity<>(exceptionDto, HttpStatus.UNAUTHORIZED);
    }

}
