package cloud.jordaan.juan.kinetic.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cloud.jordaan.juan.kinetic.application.exception.ApplicationException;
import reactor.core.publisher.Mono;

@Configuration
@Order(-2)
public class ErrorHandler implements ErrorWebExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(ErrorHandler.class);

	private ObjectMapper objectMapper;

	public ErrorHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	// TODO - This could be better :/
	@Override
	public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
		throwable.printStackTrace();
		logger.error(throwable.getMessage());
		DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
		if (throwable instanceof ApplicationException) {
			logger.info("Return ApplciationException to client :" + throwable.getMessage());
			serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			DataBuffer dataBuffer = null;
			try {
				dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(throwable.getMessage()));
			} catch (JsonProcessingException e) {
				dataBuffer = bufferFactory.wrap("".getBytes());
			}
			serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
		} else if (throwable instanceof WebExchangeBindException) {
			serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			final DataBuffer dataBuffer = bufferFactory.allocateBuffer();

			((WebExchangeBindException)throwable).getAllErrors()
			.forEach(i-> {
				String message = i.getDefaultMessage();
				System.out.println("code = " + i.getCode());
				System.out.println("dm = " + i.getDefaultMessage());
				System.out.println("on = " + i.getObjectName());
				dataBuffer.write(i.getDefaultMessage().getBytes());
			});
			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
		} else if (throwable instanceof ResponseStatusException) {
			serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			DataBuffer dataBuffer = null;
			try {
				dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(((ResponseStatusException)throwable).getMessage()));
			} catch (JsonProcessingException e) {
				dataBuffer = bufferFactory.wrap("".getBytes());
			}
			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
		} else if ( throwable instanceof DataIntegrityViolationException) {
			serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
			DataBuffer dataBuffer = null;
			try {
				dataBuffer = bufferFactory.wrap(objectMapper.writeValueAsBytes(((DataIntegrityViolationException)throwable).getRootCause().getMessage()));
			} catch (JsonProcessingException e) {
				dataBuffer = bufferFactory.wrap("".getBytes());
			}
			return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
		}

		logger.info("Return INTERNAL_SERVER_ERROR to client :" + throwable.getMessage());
		serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		serverWebExchange.getResponse().getHeaders().setContentType(MediaType.TEXT_EVENT_STREAM);
		DataBuffer dataBuffer = bufferFactory.wrap(throwable.getMessage().getBytes());

		return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
	}
}
