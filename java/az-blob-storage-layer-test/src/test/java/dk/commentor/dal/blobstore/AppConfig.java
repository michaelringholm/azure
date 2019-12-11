package dk.commentor.dal.blobstore;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ 
    //"classpath:persistence-${envTarget:mysql}.properties"
    "classpath:application.properties"
})
public class AppConfig {    
}