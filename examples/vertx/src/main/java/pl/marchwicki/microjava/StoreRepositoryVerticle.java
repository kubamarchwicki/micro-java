package pl.marchwicki.microjava;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import java.util.Iterator;

public class StoreRepositoryVerticle extends Verticle {
    final static String DB_HANDLER_NAME = "persistor";
    final static String GET_ALL = "store.getAll";
    final static String SAVE = "store.save";
    final static String UPDATE = "store.update";
    final static String DELETE = "store.delete";

    public void start() {

        JsonObject persistorCfg = new JsonObject()
                .putString("address", DB_HANDLER_NAME)
                .putString("driver", com.mysql.jdbc.Driver.class.getName())
                .putString("url", "jdbc:mysql://localhost:3306/microjava")
                .putString("username", "root")
                .putString("password", "root");

        vertx.eventBus().registerHandler(DB_HANDLER_NAME + ".ready", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> event) {
                container.logger().info("Deployed JDBC module (name=" + DB_HANDLER_NAME + ")? " + event.body().getString("status"));
                vertx.eventBus().unregisterHandler("persistor.ready", this);
            }
        });
        container.deployModule("com.bloidonia~mod-jdbc-persistor~2.1.3", persistorCfg);

        vertx.eventBus().registerHandler(GET_ALL, (event) -> {

            vertx.eventBus().send(StoreRepositoryVerticle.DB_HANDLER_NAME, new JsonObject()
                    .putString("action", "select")
                    .putString("stmt", "SELECT * FROM todos"), (Message<JsonObject> dbevent) -> {
                JsonArray result = new JsonArray();

                container.logger().info(dbevent.body().getArray("result"));

                Iterator<Object> i = dbevent.body().getArray("result").iterator();
                while (i.hasNext()) {
                    JsonObject obj = (JsonObject) i.next();
                    result.addObject(new JsonObject()
                            .putNumber("id", obj.getNumber("todo_id"))
                            .putString("title", obj.getString("todo_title"))
                            .putBoolean("completed", obj.getBoolean("todo_completed"))
                            .putNumber("order", obj.getNumber("todo_order")));
                }

                container.logger().info(result);
                event.reply(result);
            });
        });

        vertx.eventBus().registerHandler(SAVE, (Message<JsonObject> msg) -> {
            final JsonObject todo = msg.body();

            vertx.eventBus().send(StoreRepositoryVerticle.DB_HANDLER_NAME, new JsonObject()
                    .putString("action", "insert")
                    .putString("stmt", "INSERT INTO todos (todo_title, todo_completed, todo_order) values (?, ?, ?)")
                    .putArray("values", new JsonArray()
                                    .addString(todo.getString("title"))
                                    .addBoolean(todo.getBoolean("completed"))
                                    .addNumber(todo.getNumber("order"))), (Message<JsonObject> dbevent) -> {

                container.logger().info(dbevent.body());
                if (!dbevent.body().getString("status").equals("ok")) {
                    msg.fail(-1, "Failed to insert element: " + dbevent.body().getString("message"));
                } else {
                    JsonObject idObject = dbevent.body().getArray("result").get(0);
                    msg.reply(todo.putNumber("id", idObject.getNumber("GENERATED_KEY")));
                }
            });

        });

        vertx.eventBus().registerHandler(UPDATE, (Message<JsonObject> msg) -> {
            final JsonObject todo = msg.body();

            vertx.eventBus().send(StoreRepositoryVerticle.DB_HANDLER_NAME, new JsonObject()
                    .putString("action", "update")
                    .putString("stmt", "UPDATE todos SET todo_title = ?, todo_completed = ?, todo_order = ? WHERE todo_id = ?")
                    .putArray("values", new JsonArray()
                            .addString(todo.getString("title"))
                            .addBoolean(todo.getBoolean("completed"))
                            .addNumber(todo.getNumber("order"))
                            .addNumber(todo.getNumber("id"))), (Message<JsonObject> dbevent) -> {

                container.logger().info(dbevent.body());
                if (!dbevent.body().getString("status").equals("ok")) {
                    msg.fail(-1, "Failed to insert element: " + dbevent.body().getString("message"));
                } else {
                    msg.reply();
                }
            });

        });

        vertx.eventBus().registerHandler(DELETE, (Message<String> msg) -> {
            String todoId = msg.body();

            vertx.eventBus().send(StoreRepositoryVerticle.DB_HANDLER_NAME, new JsonObject()
                    .putString("action", "update")
                    .putString("stmt", "DELETE FROM todos where todo_id = ?")
                    .putArray("values", new JsonArray().addNumber(new Long(todoId))), (Message<JsonObject> dbevent) -> {

                if (!dbevent.body().getString("status").equals("ok")) {
                    msg.fail(-1, "Failed to delete element: " + dbevent.body().getString("message"));
                } else {
                    msg.reply();
                }
            });
        });

    }
}
