package S05T2CebanNicusor.JuegoDados.model.exception;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String playerName) {
        super("Player already exists with name: " + playerName);
    }
}
