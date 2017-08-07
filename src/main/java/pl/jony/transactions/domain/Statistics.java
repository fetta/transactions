package pl.jony.transactions.domain;

import lombok.Value;

@Value
public class Statistics {
  private double sum;
  private double avg;
  private double max;
  private double min;
  private int count;
}
