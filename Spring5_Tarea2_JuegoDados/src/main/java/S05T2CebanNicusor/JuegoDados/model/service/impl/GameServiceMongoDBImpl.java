package S05T2CebanNicusor.JuegoDados.model.service.impl;

import S05T2CebanNicusor.JuegoDados.model.dto.GameDTO;
import S05T2CebanNicusor.JuegoDados.model.domain.Game;
import S05T2CebanNicusor.JuegoDados.model.exception.NoGamesFoundException;
import S05T2CebanNicusor.JuegoDados.model.mapper.GameMapper;
import S05T2CebanNicusor.JuegoDados.model.repository.mongodb.GameRepository;
import S05T2CebanNicusor.JuegoDados.model.repository.mysql.PlayerRepository;
import S05T2CebanNicusor.JuegoDados.model.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceMongoDBImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository; // Añadido para verificar la existencia del jugador
    private final GameMapper gameMapper;

    // Nuevo método para verificar si el jugador existe
    public boolean playerExists(int playerId) {
        return playerRepository.existsById(playerId);
    }

    @Override
    public GameDTO newGame(int playerId) {
        Random random = new Random();
        int diceRoll1 = random.nextInt(6) + 1;
        int diceRoll2 = random.nextInt(6) + 1;
        int result = diceRoll1 + diceRoll2;
        return GameDTO.builder()
                .diceRoll1(diceRoll1)
                .diceRoll2(diceRoll2)
                .result(result)
                .win(result == 7)
                .playerId(playerId)
                .build();
    }

    @Override
    public GameDTO addGame(int playerId) {
        return gameMapper.convertToDTO(gameRepository.save(gameMapper.convertToEntity(newGame(playerId))));
    }

    @Override
    public List<GameDTO> getAllGames(int playerId, boolean throwIfEmpty) {
        List<Game> games = gameRepository.findAllByPlayerId(playerId);

        if (games.isEmpty() && throwIfEmpty) {
            throw new NoGamesFoundException("No games found: player with ID " + playerId + " has no games yet");
        }

        return games.stream()
                .map(gameMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteGames(int playerId) {
        List<Game> games = gameRepository.findAllByPlayerId(playerId);

        if (games.isEmpty()) {
            throw new NoGamesFoundException("No games found to delete for player with ID: " + playerId);
        }

        gameRepository.deleteAllByPlayerId(playerId);
    }
}
