package S05T2CebanNicusor.JuegoDados.model.service.impl;

import S05T2CebanNicusor.JuegoDados.model.domain.Player;
import S05T2CebanNicusor.JuegoDados.model.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public Integer extractPlayerId(String token) {
        return extractClaim(token, claims -> claims.get("playerId", Integer.class));
    }

    @Override
    public String extractPlayerName(String token) {
        // Cambia 'subject' si usas un nombre distinto para almacenar el nombre del jugador
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        // Suponiendo que 'userDetails' es de tipo Player y contiene el método getPlayerId()
        Map<String, Object> claims = new HashMap<>();
        claims.put("playerId", ((Player) userDetails).getPlayerId()); // Añadir ID del jugador en el token
        return createToken(claims, userDetails);
    }

    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final Integer playerId = extractPlayerId(token);
        // Suponiendo que 'userDetails' es de tipo Player y contiene el método getPlayerId()
        return (playerId.equals(((Player) userDetails).getPlayerId()) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername()) // Aquí se podría usar el ID del usuario en lugar del nombre si se prefiere
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Key getSecretKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
}
