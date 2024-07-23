package S05T2CebanNicusor.JuegoDados.model.repository.mysql;

import S05T2CebanNicusor.JuegoDados.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> findPlayerByPlayerNameIgnoreCase(String playerName);
}