package com.example.backend.domain.protests;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProtestType {
    @JsonProperty("short")
    SHORT,

    @JsonProperty("long")
    LONG
}
