package uz.dtc.keycloakclient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    static String clientId = "spring-boot-api";
    static String clientSecret = "18yMfylP78OrJLf1GdQEoiTUHxXn6Uym";
    static String url = "http://localhost:7000";
    static String realm = "spring-demo-realm";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/get-token")
    @Produces("application/json")
    public Object getToken(@RequestParam String username, @RequestParam String password) throws Exception {
        Keycloak keycloak = getKeycloak(new AuthPropsDTO(username, password));
        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();

        String payload = accessToken.getToken().split("\\.")[1];
        String decoded = new String(Base64.getDecoder().decode(payload));

        // Parse and pretty-print JSON
        Object json = objectMapper.readValue(decoded, Object.class);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        String payloadJwt = writer.writeValueAsString(json);

        System.out.println("payloadJwt = " + payloadJwt);

        return accessToken;
    }

    @GetMapping("/refresh-token")
    public AccessTokenResponse refresh(String refreshToken) {
        return null;
    }

    public static Keycloak getKeycloak(AuthPropsDTO authProps) {
        return KeycloakBuilder
                .builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .serverUrl(url)
                .realm(realm)
                .username(authProps.username())
                .password(authProps.password())
                .grantType(OAuth2Constants.PASSWORD)

//                .resteasyClient(new ResteasyClientBuilderImpl()
//                        .connectionPoolSize(20)
//                        .build())
                .build();
    }

    public record AuthPropsDTO(String username, String password) {

    }

    //eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJFNm9xQ0xidzhSaFlraWdlYlllZmFGNXIwVEtibmN5VnVGTDNocVZjSk5nIn0.eyJleHAiOjE3NTM5NTg0MzMsImlhdCI6MTc1Mzk1ODEzMywianRpIjoiZDVmYWMwZmEtZTY2OS00MzkzLTk0ZWUtNjE0OTg2ZDQ0N2NhIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo3MDAwL3JlYWxtcy9zcHJpbmctZGVtby1yZWFsbSIsImF1ZCI6WyJzcHJpbmctYm9vdC1hcGkiLCJhY2NvdW50Il0sInN1YiI6Ijg5NTM3OTM1LWU3NjgtNGQzMy1hYzJkLTUwMjI3OGY5YzZmMSIsInR5cCI6IkJlYXJlciIsImF6cCI6InNwcmluZy1ib290LWFwaSIsInNlc3Npb25fc3RhdGUiOiIxZWQxNjBmZi0zMWFhLTQ0ZDUtYjYwZC05ZmY5ZWUzYzQ4MDQiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbIi8qIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1zcHJpbmctZGVtby1yZWFsbSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInNpZCI6IjFlZDE2MGZmLTMxYWEtNDRkNS1iNjBkLTlmZjllZTNjNDgwNCIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiam9obmJlayBqb2hub3YiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huMTIzIiwiZ2l2ZW5fbmFtZSI6ImpvaG5iZWsiLCJmYW1pbHlfbmFtZSI6ImpvaG5vdiIsImVtYWlsIjoiam9obkBnbWFpbC5jb20ifQ.u0UCw5lR0tCsYUALCPRyv51YJHKsX2gFMs3SkxoQ6IWPTY91WHyhFac4A_RG6kPy71PWSP9K6msj9Y0CCX8d42PdhlhAQq3NpzkZxIDfKv0ArRtuvUOweomWBA25JbHa_Nc8ctPkrh3To2LLj8dr0LBsAr9zxTHO80t_dq8VThBX46fU7yf56kEXo-FG8kSFBzJFlFs7ga2UbVFv3hDl7suM7cpO3EM5z1OHGJ7cVGyyyYcnSgMZTjbtvmrA3C3mchWkTr9UUtBpKJQctzxNZtf7J5s2YUriS8z6HLJypAn6qcVvX0I9NCJLLIsr6YEfIs5N0aLJrXMttn828BTKYg

}
