package com.graphqljava.tutorial.model.retail;

import java.util.UUID;

public record account (
    UUID id,
    String name,
    String created_at,
    String updated_at
) {
}
