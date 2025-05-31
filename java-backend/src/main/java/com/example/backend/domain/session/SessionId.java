package com.example.backend.domain.session;
import lombok.*;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SessionId implements Serializable {

    private String weekCnt;
    private String childId;
}