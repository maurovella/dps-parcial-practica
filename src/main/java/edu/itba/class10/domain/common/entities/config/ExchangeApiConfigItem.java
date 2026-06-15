package edu.itba.class10.domain.common.entities.config;

import lombok.Builder;

@Builder
public record ExchangeApiConfigItem(String apiKey, String name, String baseUrl) {

}
