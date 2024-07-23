package S05T2CebanNicusor.JuegoDados.model.service.impl;

import S05T2CebanNicusor.JuegoDados.model.mapper.PlayerMapper;
import S05T2CebanNicusor.JuegoDados.model.domain.Player;
import S05T2CebanNicusor.JuegoDados.model.dto.PlayerDTO;
import S05T2CebanNicusor.JuegoDados.model.exception.NoPlayersFoundException;
import S05T2CebanNicusor.JuegoDados.model.exception.PlayerAlreadyExistsException;
import S05T2CebanNicusor.JuegoDados.model.exception.PlayerNotFoundException;
import S05T2CebanNicusor.JuegoDados.model.repository.mongodb.GameRepository;
import S05T2CebanNicusor.JuegoDados.model.repository.mysql.PlayerRepository;
import S05T2CebanNicusor.JuegoDados.model.service.PlayerService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceMySQLImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final PlayerMapper playerMapper;

    @Override
    public String validatePlayerName(String playerName) {
        if (StringUtils.isBlank(playerName) || playerName.equalsIgnoreCase("UNKNOWN")) {
            return "ANONIMO";
        }

        Optional<Player> existingPlayer = playerRepository.findPlayerByPlayerNameIgnoreCase(playerName);
        if (existingPlayer.isPresent()) {
            throw new PlayerAlreadyExistsException(playerName);
        }

        return playerName;
    }

    @Override
    public Optional<Player> getOptionalPlayer(int playerId) {
        return playerRepository.findById(playerId);
    }

    @Override
    public PlayerDTO getOnePlayer(int playerId) {
        Player player = getOptionalPlayer(playerId).orElseThrow(() -> new PlayerNotFoundException(playerId));

        if ("ADMIN".equalsIgnoreCase(player.getRole().toString())) {
            throw new PlayerNotFoundException(playerId);
        }

        return playerMapper.convertToDTO(player);
    }

    @Override
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();

        return players.stream()
                .filter(player -> !("ADMIN".equalsIgnoreCase(player.getRole().toString())))
                .map(playerMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDTO updatePlayerName(int id, String newName) {
        Player player = getOptionalPlayer(id).orElseThrow(() -> new PlayerNotFoundException(id));

        String validatedName = validatePlayerName(newName);
        player.setPlayerName(validatedName);

        Player updatedPlayer = playerRepository.save(player);

        return playerMapper.convertToDTO(updatedPlayer);
    }

    @Override
    public PlayerDTO deletePlayer(int idPlayer) {
        Player player = getOptionalPlayer(idPlayer).orElseThrow(() -> new PlayerNotFoundException(idPlayer));

        gameRepository.deleteAllByPlayerId(idPlayer);
        playerRepository.deleteById(idPlayer);

        return playerMapper.convertToDTO(player);
    }

    @Override
    public float getAverageWinRate() {
        List<PlayerDTO> players = getAllPlayers();

        if (players.isEmpty()) {
            throw new NoPlayersFoundException();
        }

        OptionalDouble totalWinRate = players.stream()
                .mapToDouble(PlayerDTO::getWinRate)
                .average();

        return (float) totalWinRate.orElse(0);
    }

    @Override
    public PlayerDTO getBestPlayer() {
        List<PlayerDTO> players = getAllPlayers();

        return players.stream()
                .max(Comparator.comparing(PlayerDTO::getWinRate))
                .orElseThrow(NoPlayersFoundException::new);
    }

    @Override
    public PlayerDTO getWorstPlayer() {
        List<PlayerDTO> players = getAllPlayers();

        return players.stream()
                .min(Comparator.comparing(PlayerDTO::getWinRate))
                .orElseThrow(NoPlayersFoundException::new);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<Player> player = playerRepository.findById(Integer.parseInt(username));
            return player.orElseThrow(() -> new UsernameNotFoundException("Player not found with ID: " + username));
        };
    }
}
