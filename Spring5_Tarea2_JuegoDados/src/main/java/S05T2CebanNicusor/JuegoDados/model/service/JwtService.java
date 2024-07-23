package S05T2CebanNicusor.JuegoDados.model.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    Integer extractPlayerId(String token);
    String extractPlayerName(String token); // Método añadido a la interfaz
    String generateToken(UserDetails userDetails);
    Boolean isTokenValid(String token, UserDetails userDetails);
}
