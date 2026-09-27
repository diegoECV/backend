package vallegrande.edu.pe.backend.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import vallegrande.edu.pe.backend.dto.AuthRequest;
import vallegrande.edu.pe.backend.dto.AuthResponse;
import vallegrande.edu.pe.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRest {

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        // En producción se validaría la contraseña contra la DB.
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            String token = jwtUtil.generateToken(request.getUsername());
            return Mono.just(ResponseEntity.ok(new AuthResponse(token)));
        } else {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }
}
