package S05T2CebanNicusor.JuegoDados.model.service;

import S05T2CebanNicusor.JuegoDados.model.dto.PlayerDTO;
import S05T2CebanNicusor.JuegoDados.model.domain.Player;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;
import java.util.Optional;

public interface PlayerService {
    String validatePlayerName(String playerName);
    Optional<Player> getOptionalPlayer(int id);
    PlayerDTO getOnePlayer(int id);
    List<PlayerDTO> getAllPlayers();
    PlayerDTO updatePlayerName(int id, String newName);
    PlayerDTO deletePlayer(int id);
    float getAverageWinRate();
    PlayerDTO getBestPlayer();
    PlayerDTO getWorstPlayer();
    UserDetailsService userDetailsService();
}