package no.kantega.embed;

import no.kantega.publishing.api.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

import static no.kantega.utilities.Objects.nonNull;

@Configuration
public class EmbedConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public Embedder embed(SystemConfiguration configuration){
        Embedly embedly = getEmbedly(configuration);
        if(embedly != null){
            log.info("Using Embedly");
            return embedly;
        } else {
            log.info("Using default Embedder");
            return defaultEmbedder();
        }
    }

    private Embedder defaultEmbedder() {
        return new DefaultEmbedder();
    }

    private Embedly getEmbedly(SystemConfiguration configuration) {
        Embedly embedly = null;
        try{
            URL apiUrl = new URL(configuration.getString("embed.ly.api.url"));
            String apiUrlEncoding = configuration.getString("embed.ly.api.url.encoding");
            String apiKey = configuration.getString("embed.ly.api.key");
            apiUrlEncoding = nonNull(apiUrlEncoding) ? apiUrlEncoding : "UTF-8";
            if (nonNull(apiKey)) {
                embedly = new Embedly(apiUrl, apiKey, apiUrlEncoding);
            }
        } catch (Exception cause) {

        }
        return embedly;
    }
}
