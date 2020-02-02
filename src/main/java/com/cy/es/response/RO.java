package com.cy.es.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RO {

    private int code;

    private String msg;

    public static RO success() {
        return new RO(200, "success");
    }

    public static RO fail() {
        return new RO(502, "fail");
    }

}
