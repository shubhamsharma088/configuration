package com.example.configuration;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

@Log4j2
@EnableConfigurationProperties(BootifulProperties.class)  // or @ConfigurationPropertiesScan
@SpringBootApplication
public class ConfigurationApplication {

  /**
   * Tells {@link SpringApplication} to take {@link ConfigurationApplication} as as source to build
   * the app and initialize it with a {@link ConfigurableApplicationContext} which as {@link
   * BootifulPropertySource} as once of its {@link MutablePropertySources}
   */
  public static void main(String[] args) {
    //SpringApplication.run(ConfigurationApplication.class, args);

    new SpringApplicationBuilder()
        .sources(ConfigurationApplication.class)
        /** Can be done in a flexible way using {@link ConfigurableEnvironment}
         * contributeToPropertySources

         .initializers(applicationContext -> applicationContext.getEnvironment().getPropertySources()
         .addLast(new BootifulPropertySource()))
         *
         */
        .build()
        .run(args);

  }


  @Autowired
  void contributeToPropertySources(ConfigurableEnvironment env) {
    env.getPropertySources().addLast(new BootifulPropertySource());
  }

  /**
   * This bean should <em>run</em> when it is contained within a {@link SpringApplication}.
   *
   * @param env             provides Environment Properties with {@link SpringApplication} context
   * @param defaultProp     {@link String} property to be injected
   * @param dataSource      {@link String} property to be injected
   * @param bootifulMessage {@link String} property to be injected from {@link
   *                        BootifulPropertySource}
   * @return {@link ApplicationRunner} bean
   */
  @Bean
  ApplicationRunner applicationRunner(Environment env,
      @Value("${my-default-prop:default_value}") String defaultProp,
      @Value("${spring.datasource.url}") String dataSource,
      @Value("${bootiful-messages}") String bootifulMessage,
      @Value("${remote-prop}") String configServerProp,
      @Value("${message-from-vault-server}") String messageFromVaultServer,
      BootifulProperties bootifulConfigurationProperties) {

    return args -> {
      log.info(env.getProperty("my-prop"));
      log.info(defaultProp);
      log.info(dataSource);
      log.info(bootifulMessage);
      log.info(bootifulConfigurationProperties.getMessage());
      log.info(configServerProp);
      log.info(messageFromVaultServer);
    };
  }

  static class BootifulPropertySource extends PropertySource<String> {

    public BootifulPropertySource() {
      super("bootiful");
    }

    @Override
    public Object getProperty(String name) {
      if (name.equalsIgnoreCase("bootiful-messages")) {
        return "Hello from " + BootifulPropertySource.class.getSimpleName() + " !";
      }
      return null;
    }
  }
}
