
package com.spantag.socialMediaAppln.exceptions;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spantag.socialMediaAppln.utils.ResponseInfo;
import com.spantag.socialMediaAppln.utils.commonUtils;
import com.spantag.socialMediaAppln.utils.ResponseInfo.ResponseType;

@ControllerAdvice
@SuppressWarnings({ "rawtypes" })
public class CustomExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

	commonUtils common = new commonUtils();

	@ExceptionHandler(BadCredentialsException.class)
	public final ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("000", "Incorrect Username or password");

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<Object> handleUserNameNotFoundExceptions(UsernameNotFoundException ex, WebRequest request) {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("000", ex.getMessage());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(FileStorageException.class)
	public final ResponseEntity<Object> handleFileStorageExceptions(FileStorageException ex, WebRequest request) {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("1000", ex.getMessage());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.INSUFFICIENT_STORAGE);
	}

	@ExceptionHandler(ClassNotFoundException.class)
	public final ResponseEntity<Object> handleClassNotFoundExceptions(ClassNotFoundException ex, WebRequest request) {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("2000", ex.getMessage());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public final ResponseEntity<Object> handleJsonParsingExceptions(JsonProcessingException ex, WebRequest request) {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("9000", ex.getMessage());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RecordNotFoundException.class)
	public final ResponseEntity<Object> handleRecordsNotAvailableExceptions(Exception ex, WebRequest request) throws Throwable {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("100009", ex.getMessage());

		System.out.println(ex.getStackTrace()[0].getLineNumber());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(SQLException.class)
	public final ResponseEntity<Object> handleAllSQLExceptions(Exception ex, WebRequest request) throws Throwable {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());
		
		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("48000", "Process Failed due to some internal error");

		System.out.println(ex.getStackTrace()[0].getLineNumber());

		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) throws Throwable {

		log.error("EXCEPTION------------OOPS ! AN EXCEPTION OCCURED------------" + ex.toString() + " request URL--------" + request.toString() + "------------ Line Number" + ex.getStackTrace()[0].getLineNumber() + "-------Method " + Thread.currentThread().getStackTrace()[1].getMethodName()+
				"Class Name-------" + Thread.currentThread().getStackTrace()[1].getClassName());

		ex.printStackTrace();

		ErrorResponse error = new ErrorResponse("12000", "An internal Error Occurred ,Kindly Contact your system Administrator");
		
		List li = common.convertToList(error);

		return new ResponseEntity<Object>(new ResponseInfo(ResponseType.FAIL, error), HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
