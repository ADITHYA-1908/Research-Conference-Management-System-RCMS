package com.rcms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class RcmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RcmsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoLaunchBrowser() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start http://localhost:8080");
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open http://localhost:8080");
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open http://localhost:8080");
            } else if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        } catch (Exception ignored) {
            // System will log access URL to console
        }
    }
}
