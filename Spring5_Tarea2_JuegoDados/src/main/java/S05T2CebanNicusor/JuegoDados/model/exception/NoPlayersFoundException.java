package S05T2CebanNicusor.JuegoDados.model.exception;

public class NoPlayersFoundException extends RuntimeException {
    public NoPlayersFoundException() {
        super("No players found");
    }
}
