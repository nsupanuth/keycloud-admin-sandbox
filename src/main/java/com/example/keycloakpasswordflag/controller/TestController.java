package com.example.keycloakpasswordflag.controller;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TestController {

    @GetMapping("/update/password")
    public void testUpdatePassword() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("{host-name}/auth")
                .realm("FillMeIn")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId("fill-me-in-app")
                .clientSecret("serect")
                .username("admin") //the admin user for realm
                .password("admin")
                .build();

        UsersResource usersResource = keycloak.realm("FillMeIn").users();
        List<UserRepresentation> keycloakUsers = usersResource.list();

        Optional<UserRepresentation> userOpt = keycloakUsers.stream().filter(user -> user.getUsername().equals("testuser1")).findAny();

        if (userOpt.isPresent()) {
            UserRepresentation usr = userOpt.get();
            if (usr.getRequiredActions().contains("UPDATE_PASSWORD")){
                System.out.println("Test");

                // Setting the new credential
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue("newpassword");

                UserResource userResource = usersResource.get(usr.getId());
                // clear required action
//                userResource.toRepresentation().setRequiredActions(new ArrayList<>());
                // reset password
                userResource.resetPassword(credential);
            }
        }

    }


}
