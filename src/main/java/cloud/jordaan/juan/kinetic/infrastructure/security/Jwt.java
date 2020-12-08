package cloud.jordaan.juan.kinetic.infrastructure.security;

public class Jwt {
	public static final String XAUTH_TOKEN_TYPE = "X-Auth";
	public static final String BEARER_TOKEN_TYPE = "Bearer ";
	public static final String ISSUER = "juan.jordaan.cloud";

    //public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
//    public static final String SIGNING_KEY = "devglan123r";
    public static final String AUTHORITIES_KEY = "scopes";
}
