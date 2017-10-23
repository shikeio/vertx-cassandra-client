package io.vertx.ext.cassandra.impl;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.Map;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.cassandra.CQLRowStream;
import io.vertx.ext.cassandra.CassandraSession;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class CassandraSessionImpl implements CassandraSession {

  private Session session;

  CassandraSessionImpl(Session session) {
    this.session = session;
  }

  @Override
  public String getLoggedKeyspace() {
    return session.getLoggedKeyspace();
  }

  @Override
  public CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler) {
    Futures.addCallback(session.executeAsync(query), new FutureCallback<ResultSet>() {
      @Override
      public void onSuccess(ResultSet result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler, Object... values) {
    Futures.addCallback(session.executeAsync(query, values), new FutureCallback<ResultSet>() {
      @Override
      public void onSuccess(ResultSet result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler, Map<String, Object> values) {
    Futures.addCallback(session.executeAsync(query, values), new FutureCallback<ResultSet>() {
      @Override
      public void onSuccess(ResultSet result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession execute(Statement statement, Handler<AsyncResult<ResultSet>> handler) {
    Futures.addCallback(session.executeAsync(statement), new FutureCallback<ResultSet>() {
      @Override
      public void onSuccess(ResultSet result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession prepare(String query, Handler<AsyncResult<PreparedStatement>> handler) {
    Futures.addCallback(session.prepareAsync(query), new FutureCallback<PreparedStatement>() {
      @Override
      public void onSuccess(PreparedStatement result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession prepare(RegularStatement statement, Handler<AsyncResult<PreparedStatement>> handler) {
    Futures.addCallback(session.prepareAsync(statement), new FutureCallback<PreparedStatement>() {
      @Override
      public void onSuccess(PreparedStatement result) {
        handler.handle(Future.succeededFuture(result));
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
    return this;
  }

  @Override
  public CassandraSession queryStream(Statement statement, Handler<AsyncResult<CQLRowStream>> handler) {
    execute(statement, event -> {
      if (event.succeeded()) {
        handler.handle(Future.succeededFuture(new CQLRowStreamImpl(Vertx.currentContext(), event.result())));
      }
    });
    return this;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    Futures.addCallback(session.closeAsync(), new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        handler.handle(Future.succeededFuture());
      }

      @Override
      public void onFailure(Throwable t) {
        handler.handle(Future.failedFuture(t));
      }
    });
  }
}
