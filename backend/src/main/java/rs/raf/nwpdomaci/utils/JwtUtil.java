package rs.raf.nwpdomaci.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.raf.nwpdomaci.model.Permission;
import rs.raf.nwpdomaci.model.User;

import java.util.*;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "MY JWT SECRET";

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("sub", user.getEmail());
        claims.put("permissions", user.getPermissions());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }

    public String getTokenFromAuthorization(String authorization) {
        if(authorization.startsWith("Bearer "))
            return authorization.split(" ")[1];

        System.err.println("Authorization invalid (no bearer found)");
        return null;
    }

//    private List<String> extractPermissionNames(Set<Permission> permissionSet) {
//        List<String> permissionNames = new ArrayList<>();
//        for(Permission permission: permissionSet) {
//            permissionNames.add(permission.getName());
//        }
//
//        return permissionNames;
//    }


}
