package org.pickwicksoft.libraary.config;

import org.pickwicksoft.bookgrabber.BookGrabber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookGrabberConfig {

    @Bean
    public BookGrabber bookGrabber() {
        return new BookGrabber();
    }
}
