package pl.marchwicki.microjava;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

import java.io.InputStream;
import java.util.Scanner;

public class StaticFilesVerticle extends Verticle {

    final static String HANDLER_NAME = "staticFiles";

    public void start() {

        vertx.eventBus().registerHandler(HANDLER_NAME, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> event) {
                String path = event.body();

                InputStream stream = TodoMVCVerticle.class.getResourceAsStream("/META-INF/resources" + path);
                if (stream != null) {
                    container.logger().info("File " + path + " found");

                    String output = new Scanner(stream, "UTF-8").useDelimiter("\\A").next();
                    event.reply(output);
                } else {
                    event.fail(-1, "File not found");
                }

            }
        });

    }

}
