package org.example.model;

import lombok.Data;

@Data
public class PongDTO {


    private int clientId;

    public String clientInfo(){
        return " clientId: " + clientId;
    }
}
