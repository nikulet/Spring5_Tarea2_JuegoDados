package S05T2CebanNicusor.JuegoDados.model.service;

import S05T2CebanNicusor.JuegoDados.model.dto.request.SignInRequest;
import S05T2CebanNicusor.JuegoDados.model.dto.request.SignUpRequest;
import S05T2CebanNicusor.JuegoDados.model.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);
    AuthenticationResponse signIn(SignInRequest request);
}