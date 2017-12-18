/**
 * 
 */
package com.oauth.task.accountregistration.exception;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.oauth.task.accountregistration.model.RestErrorInfo;

/**
 * @author SUMAN
 *
 */
@RestControllerAdvice
public abstract class ServiceExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

	    @ExceptionHandler(value = {DataFormatException.class})
	    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	    public RestErrorInfo handleDataStoreException(DataFormatException ex, WebRequest request, HttpServletResponse response) {
	        LOGGER.info("Converting Data Store exception to RestResponse : " + ex.getMessage());

	        return new RestErrorInfo(ex, "You messed up.");
	    }

	    @ResponseStatus(value = HttpStatus.NOT_FOUND)
	    @ExceptionHandler(ResourceNotFoundException.class)
	    @ResponseBody
	    public RestErrorInfo handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request, HttpServletResponse response) {
	        LOGGER.info("ResourceNotFoundException handler:" + ex.getMessage());

	        return new RestErrorInfo(ex, "Sorry I couldn't find it.");
	    }


}
