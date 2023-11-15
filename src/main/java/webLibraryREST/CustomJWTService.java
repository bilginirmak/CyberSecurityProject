package webLibraryREST;

import com.prog2.labs.db.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomJWTService implements ICustomJWTService {

    private JWTTokenOptions jwtTokenOptions;

    public CustomJWTService(JWTTokenOptions jwtTokenOptions) {
        this.jwtTokenOptions = jwtTokenOptions;
    }

    @Override
    public String GetToken(User user) {
      
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUser_id());
        claims.put("name", user.getName());
        claims.put("role", user.getRole().toString());
        claims.put("login", user.getLogin());
        claims.put("contact", user.getContact());
      

        SecretKey key = Keys.hmacShaKeyFor(jwtTokenOptions.getSecurityKey().getBytes());
        
        
        @SuppressWarnings("deprecation")
		String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtTokenOptions.getIssuer())
                .setAudience(jwtTokenOptions.getAudience())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 300000)) 
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return jwtToken;
    }
}
