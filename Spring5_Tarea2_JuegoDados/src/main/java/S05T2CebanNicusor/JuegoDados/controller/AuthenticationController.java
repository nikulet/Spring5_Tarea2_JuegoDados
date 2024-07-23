package S05T2CebanNicusor.JuegoDados.controller;

import S05T2CebanNicusor.JuegoDados.model.dto.request.SignInRequest;
import S05T2CebanNicusor.JuegoDados.model.dto.request.SignUpRequest;
import S05T2CebanNicusor.JuegoDados.model.dto.response.AuthenticationResponse;
import S05T2CebanNicusor.JuegoDados.model.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Sign up: create new player")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    @Operation(summary = "Sign in as player")
    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}