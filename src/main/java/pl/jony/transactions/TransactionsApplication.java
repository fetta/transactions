package pl.jony.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class TransactionsApplication {
  @Bean
  public ObjectMapper jacksonBuilder() {
    Jackson2ObjectMapperBuilder b = new Jackson2ObjectMapperBuilder();
    b.indentOutput(true);
    return b.build();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(TransactionsApplication.class, args);
  }
}
