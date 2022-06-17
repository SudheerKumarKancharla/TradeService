package com.client.pojo;

/**
 * Class to represent the Status of the Trade
 */
public class StatusDTO {

    private String status;

    public StatusDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
