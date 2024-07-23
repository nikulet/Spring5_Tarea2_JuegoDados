package S05T2CebanNicusor.JuegoDados.model.mapper;

import S05T2CebanNicusor.JuegoDados.model.service.impl.GameServiceMongoDBImpl;
import S05T2CebanNicusor.JuegoDados.model.domain.Player;
import S05T2CebanNicusor.JuegoDados.model.dto.GameDTO;
import S05T2CebanNicusor.JuegoDados.model.dto.PlayerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayerMapper {

    private final GameServiceMongoDBImpl gameService;

    public PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = PlayerDTO.builder()
                .playerId(player.getPlayerId())
                .playerName(player.getPlayerName())
                .password(player.getPassword())
                .role(player.getRole())
                .registrationDate(player.getRegistrationDate())
                .winRate(getPlayerWinRate(player.getPlayerId()))
                .build();

        return dto;
    }

    public Player convertToEntity(PlayerDTO dto) {
        return Player.builder()
                .playerName(dto.getPlayerName())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public float getPlayerWinRate(int id) {
        List<GameDTO> games = gameService.getAllGames(id, false);

        return games.isEmpty() ? 0 :
                (float) games.stream()
                        .filter(GameDTO::isWin)
                        .count() / games.size();
    }
}
