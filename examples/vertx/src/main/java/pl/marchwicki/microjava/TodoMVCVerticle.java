package pl.marchwicki.microjava;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class TodoMVCVerticle extends Verticle {

    public void start() {

        container.deployVerticle(StoreRepositoryVerticle.class.getName());
        container.deployWorkerVerticle(StaticFilesVerticle.class.getName());

        RouteMatcher matcher = new RouteMatcher();
        matcher.get("/todos", (httpServerRequest) -> {
            vertx.eventBus().send(StoreRepositoryVerticle.GET_ALL, "", (Message<JsonArray> event) -> {
                httpServerRequest.response()
                        .putHeader("Content-Type", "application/json")
                        .setStatusCode(200)
                        .end(event.body().encodePrettily());
            });
        });

        matcher.post("/todos", (httpServerRequest) -> {
            httpServerRequest.bodyHandler((buffer) -> {
                String body = buffer.toString();

                vertx.eventBus().sendWithTimeout(StoreRepositoryVerticle.SAVE, new JsonObject(body), 1000, (AsyncResult<Message<JsonObject>> event) -> {
                    HttpServerResponse response = httpServerRequest.response();
                    if (event.failed()) {
                        ReplyException rx = (ReplyException) event.cause();
                        response.setStatusCode(500)
                                .setStatusMessage(rx.getMessage()).end();
                    } else {
                        response.setStatusCode(201).end(event.result().body().encodePrettily());
                    }
                });

            });
        });

        matcher.put("/todos/:id", (httpServerRequest) -> {
            httpServerRequest.bodyHandler((buffer) -> {
                String body = buffer.toString();

                vertx.eventBus().sendWithTimeout(StoreRepositoryVerticle.UPDATE, new JsonObject(body), 1000, (AsyncResult<Message<JsonObject>> event) -> {
                    HttpServerResponse response = httpServerRequest.response();
                    if (event.failed()) {
                        ReplyException rx = (ReplyException) event.cause();
                        response.setStatusCode(500)
                                .setStatusMessage(rx.getMessage()).end();
                    } else {
                        response.setStatusCode(204).end();
                    }
                });

            });

        });

        matcher.delete("/todos/:id", (httpServerRequest) -> {
            String todoId = httpServerRequest.params().get("id");
            vertx.eventBus().sendWithTimeout(StoreRepositoryVerticle.DELETE, todoId, 1000, (event) -> {

                HttpServerResponse response = httpServerRequest.response();
                if (event.failed()) {
                    ReplyException rx = (ReplyException) event.cause();
                    response.setStatusCode(500)
                            .setStatusMessage(rx.getMessage());
                } else {
                    response.setStatusCode(204);
                }
                response.end();
            });
        });

        matcher.getWithRegEx(".*", (req) -> {
            String path;
            if (req.path().equals("/")) {
                path = "/index.html";
            } else {
                path = req.path();
            }
            container.logger().info("Static file for path " + path);

            vertx.eventBus().sendWithTimeout(StaticFilesVerticle.HANDLER_NAME, path, 3000, (AsyncResult<Message<String>> event) -> {
                if (event.failed()) {
                    ReplyException ex = (ReplyException) event.cause();
                    req.response().setStatusCode(404).end(ex.getMessage());
                    return;
                }

                req.response().end(event.result().body());
            });
        });

        vertx.createHttpServer().requestHandler(matcher).listen(8888);
        container.logger().info("TodoMVCVerticle started");

    }
}
