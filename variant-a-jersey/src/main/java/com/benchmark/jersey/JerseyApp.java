package com.benchmark.jersey;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class JerseyApp {
    public static void main(String[] args) {
        URI baseUri = URI.create("http://0.0.0.0:8080/api/");
        ResourceConfig config = new ResourceConfig()
            .packages("com.benchmark.jersey.resource");
        
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        System.out.println("Jersey server started on http://localhost:8080/api");
        
        try {
            server.start();
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
