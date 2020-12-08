package cloud.jordaan.juan.kinetic.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import cloud.jordaan.juan.kinetic.infrastructure.security.Jwt;

public interface SecurityUtil {
	public static String getAuthCookie(ResponseEntity<?> response) {
		String cookie =  response
							.getHeaders()
							.getFirst(HttpHeaders.SET_COOKIE);
		if(cookie != null) {
			cookie = cookie.substring(Jwt.XAUTH_TOKEN_TYPE.length() + 1);

			return cookie.substring(0, cookie.indexOf(';'));
		}
		return null;
	}

	public static <T> HttpEntity<T> setAuthCookie(String cookie) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, Jwt.BEARER_TOKEN_TYPE+cookie);

		return new HttpEntity<T>(null, headers);
	}

	public static <T> HttpEntity<T> setAuthCookie(T body, String cookie) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, Jwt.BEARER_TOKEN_TYPE+cookie);

		return new HttpEntity<T>(body, headers);
	}
}
