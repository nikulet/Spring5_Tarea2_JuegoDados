package S05T2CebanNicusor.JuegoDados.controller;

import lombok.Getter;
import lombok.Setter;

// DTO para la solicitud de actualización del nombre del jugador
@Setter
@Getter
public class UpdatePlayerNameRequest {
    private String playerName;

}
