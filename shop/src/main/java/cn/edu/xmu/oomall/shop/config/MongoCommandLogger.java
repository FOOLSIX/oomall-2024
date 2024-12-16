package cn.edu.xmu.oomall.shop.config;

import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoCommandLogger implements CommandListener {
    @Override
    public void commandStarted(CommandStartedEvent event) {
        log.info("Command: " + event.getCommandName());
        log.info("Connection Address: " + event.getConnectionDescription().getServerAddress());
    }
}
