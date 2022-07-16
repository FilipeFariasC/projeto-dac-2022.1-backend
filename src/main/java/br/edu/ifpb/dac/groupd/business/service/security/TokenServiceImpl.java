package br.edu.ifpb.dac.groupd.business.service.security;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.business.service.interfaces.TokenService;
import br.edu.ifpb.dac.groupd.model.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenServiceImpl implements TokenService{
	
	@Value("${jwt.token.secret}")
	private String secret;
	
	@Value("${jwt.token.expiration}")
	private String expiration;
	
	private static final long EXPIRATION_TIME = 1000L * 60L * 60L * 24L * 7L;  // 1 semana
	
	private static final String CLAIM_USERID = "userid";
	private static final String CLAIM_USERNAME = "username";
	private static final String CLAIM_EXPIRATION = "expirationTime";
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(getExpirationTime()))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	
    private Long getExpirationTime() {
    	return System.currentTimeMillis() + EXPIRATION_TIME;
    }

	@Override
	public String generate(User user) {
		long expiration = Long.valueOf(this.expiration);
		
		LocalDateTime expirationLocalDateTime = LocalDateTime.now().plusDays(expiration);
		Instant expirationInstant = expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
		Date expirationDate = Date.from(expirationInstant);
		
		String tokenExpiration = expirationLocalDateTime
				.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				
		
		return Jwts
			.builder()
			.setExpiration(expirationDate)
			.setSubject(user.getId().toString())
			.claim(CLAIM_USERNAME, user.getUsername())
			.claim(CLAIM_EXPIRATION, tokenExpiration)
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

	@Override
	public Claims getClaims(String token) throws ExpiredJwtException {
		// TODO Auto-generated method stub
		return Jwts
			.parser()
			.setSigningKey(secret)
			.parseClaimsJws(token)
			.getBody();
	}

	@Override
	public boolean isValid(String token) {
		if(token == null) {
			return false;
		}
		
		try {
		Claims claims = getClaims(token);
			LocalDateTime expirationDate = claims
				.getExpiration()
				.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		
			return !LocalDateTime.now().isAfter(expirationDate);
		} catch(Exception exception) {
			return false;
		}
	}

	@Override
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		
		return claims.get(CLAIM_USERNAME).toString();
	}

	@Override
	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		return Long.parseLong(claims.getSubject());
	}

	@Override
	public String get(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		
		if(authorization == null || !authorization.startsWith("Bearer ")) return null;
		
		return authorization.substring(7);
	}
}
