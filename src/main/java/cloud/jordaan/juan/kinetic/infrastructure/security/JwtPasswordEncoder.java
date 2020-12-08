package cloud.jordaan.juan.kinetic.infrastructure.security;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtPasswordEncoder implements PasswordEncoder {
	private Logger logger = LoggerFactory.getLogger(JwtPasswordEncoder.class);

	@Value("${springbootwebfluxjjwt.password.encoder.secret}")
	private String secret;

	@Value("${springbootwebfluxjjwt.password.encoder.iteration}")
	private Integer iteration;

	@Value("${springbootwebfluxjjwt.password.encoder.keylength}")
	private Integer keylength;

	@Override
	public String encode(CharSequence cs) {
		try {
			byte[] result = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA512")
					.generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
					.getEncoded();

			return Base64.getEncoder().encodeToString(result);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean matches(CharSequence cs, String string) {
		boolean matches = encode(cs).equals(string);
		logger.info("Passwords match : " + matches);
		return matches;
	}
}
