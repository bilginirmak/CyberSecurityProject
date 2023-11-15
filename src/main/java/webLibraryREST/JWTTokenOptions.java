package webLibraryREST;

public class JWTTokenOptions {

	private String Audience;

	private String SecurityKey;
	private String Issuer;
	
	
	public String getAudience() {
		return Audience;
	}
	public void setAudience(String audience) {
		Audience = audience;
	}
	public String getSecurityKey() {
		return SecurityKey;
	}
	public void setSecurityKey(String securityKey) {
		SecurityKey = securityKey;
	}
	public String getIssuer() {
		return Issuer;
	}
	public void setIssuer(String issuer) {
		Issuer = issuer;
	}
	
	
}
