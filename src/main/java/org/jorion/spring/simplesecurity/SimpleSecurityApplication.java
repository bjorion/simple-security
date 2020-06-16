package org.jorion.spring.simplesecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleSecurityApplication
{
    // --- Constants ---
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SimpleSecurityApplication.class);

    public static void main(String[] args)
    {
        SpringApplication.run(SimpleSecurityApplication.class, args);
    }

}
