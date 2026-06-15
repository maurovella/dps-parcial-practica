package edu.itba.class10.domain.persistence;

import edu.itba.class10.domain.entity.money.MoneyAmount;

import java.time.LocalDate;

public record SingleConversionEntity(LocalDate date, MoneyAmount from, MoneyAmount to) {

}
