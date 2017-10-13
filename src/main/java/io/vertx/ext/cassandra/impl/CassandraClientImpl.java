package io.vertx.ext.cassandra.impl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cassandra.CassandraClient;
import io.vertx.ext.cassandra.CassandraSession;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
@SuppressWarnings("unchecked")
public class CassandraClientImpl implements CassandraClient {

  private Vertx vertx;
  private JsonObject config;

  private Cluster cluster;

  public CassandraClientImpl(Vertx vertx, JsonObject config) {
    this.vertx = vertx;
    this.config = config;
    cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
  }

  @Override
  public CassandraClient connect(Handler<AsyncResult<CassandraSession>> handler) {
    Futures.addCallback(cluster.connectAsync(), new FutureCallback<Session>() {

      @Override
      public void onSuccess(Session session) {
        handler.handle(Future.succeededFuture(new CassandraSessionImpl(session)));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraClient connect(String keyspace, Handler<AsyncResult<CassandraSession>> handler) {
    Futures.addCallback(cluster.connectAsync(keyspace), new FutureCallback<Session>() {

      @Override
      public void onSuccess(Session session) {
        handler.handle(Future.succeededFuture(new CassandraSessionImpl(session)));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    Futures.addCallback(cluster.closeAsync(), new FutureCallback() {
      @Override
      public void onSuccess(Object result) {
        handler.handle(Future.succeededFuture());
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
  }
}
