package com.ash7nly.monolith.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.sql.SQLException;

/**
 * H2 Database Server Configuration for Development.
 *
 * This starts H2 in TCP server mode so you can connect from external tools like DBeaver.
 *
 * DBeaver Connection Settings:
 * - Driver: H2 Server
 * - URL: jdbc:h2:tcp://localhost:9092/mem:ash7nly_db
 * - Username: sa
 * - Password: password
 */
@Configuration
@Profile("dev")
public class H2ServerConfig {

    private Server tcpServer;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        tcpServer = Server.createTcpServer(
                "-tcp",
                "-tcpAllowOthers",
                "-tcpPort", "9092"
        );
        return tcpServer;
    }

    @EventListener(ContextClosedEvent.class)
    public void stopServer() {
        if (tcpServer != null && tcpServer.isRunning(true)) {
            tcpServer.stop();
        }
    }
}

