package tran.compbuildbackend.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import tran.compbuildbackend.domain.user.ApplicationUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static tran.compbuildbackend.constants.fields.FieldConstants.*;
import static tran.compbuildbackend.constants.security.SecurityConstants.EXPIRATION_TIME;
import static tran.compbuildbackend.constants.security.SecurityConstants.SECRET;


@Component
public class JwtTokenProvider {

    // Generate the token
    public String generateToken(Authentication authentication) {
        ApplicationUser applicationUser = (ApplicationUser) authentication.getPrincipal();
        Date currentDate = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        /*
         * the token stores some user attributes but the token is a string.
         * we would have to cast the userId to a string to pass it into the token.
         */
        String userId = Long.toString(applicationUser.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put(ID_FIELD, userId);
        claims.put(USER_NAME_FIELD, applicationUser.getUsername());
        claims.put(FULL_NAME_FIELD, applicationUser.getFullName());

        /*
         * claims is information about the user.
         * can pass in claims one by one or by using a map.
         * note there are many ways to create a signature
         */
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch(SignatureException ex) {
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Token has expired");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    // Get user id from token
    public Long getUserIdFromJWT(String token) {
        // we are decoding the token at this point to grab the id from it.
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get(ID_FIELD);
        return Long.parseLong(id);
    }
}
