package com.example.backend.domain.protests;

import java.io.Serializable;
import java.util.Objects;

public class ProtestsDetailId implements Serializable {
    private String protestId;
    private String behaviorName;

    // 기본 생성자, equals, hashCode 필수
    public ProtestsDetailId() {}

    public ProtestsDetailId(String protestId, String behaviorName) {
        this.protestId = protestId;
        this.behaviorName = behaviorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtestsDetailId)) return false;
        ProtestsDetailId that = (ProtestsDetailId) o;
        return Objects.equals(protestId, that.protestId) &&
                Objects.equals(behaviorName, that.behaviorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protestId, behaviorName);
    }
}
