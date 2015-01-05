package pl.marchwicki.microjava;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.eventbus.ReplyException;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

public class TodoMVCVerticle extends Verticle {

    public void start() {

        container.deployWorkerVerticle(StaticFilesVerticle.class.getName());

        RouteMatcher matcher = new RouteMatcher();
        matcher.get("/todos", new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                httpServerRequest.response().end("GET Hello world!");
            }
        });

        matcher.post("/todos", new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                httpServerRequest.response().end("POST Hello world!");
            }
        });

        matcher.put("/todos/:id", new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                httpServerRequest.response().end("PUT Hello world!");
            }
        });

        matcher.delete("/todos/:id", new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                httpServerRequest.response().end("DELETE Hello world!");
            }
        });

        matcher.getWithRegEx(".*", new Handler<HttpServerRequest>() {
            @Override
            public void handle(final HttpServerRequest req) {
                String path;
                if (req.path().equals("/")) {
                    path = "/index.html";
                } else {
                    path = req.path();
                }
                container.logger().info("Static file for path " + path);

                vertx.eventBus().sendWithTimeout(StaticFilesVerticle.HANDLER_NAME, path, 3000, new Handler<AsyncResult<Message<String>>>() {
                    @Override
                    public void handle(AsyncResult<Message<String>> event) {
                        if (event.failed()) {
                            ReplyException ex = (ReplyException) event.cause();
                            req.response().setStatusCode(404).end(ex.getMessage());
                            return;
                        }

                        req.response().end(event.result().body());
                    }
                });
            }
        });

        vertx.createHttpServer().requestHandler(matcher).listen(8888);
        container.logger().info("TodoMVCVerticle started");

    }
}
