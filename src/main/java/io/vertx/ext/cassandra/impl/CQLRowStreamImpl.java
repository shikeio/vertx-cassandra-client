package io.vertx.ext.cassandra.impl;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.util.concurrent.atomic.AtomicBoolean;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.cassandra.CQLRowStream;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class CQLRowStreamImpl implements CQLRowStream {

  private final AtomicBoolean paused = new AtomicBoolean(true);
  private final AtomicBoolean ended = new AtomicBoolean(false);
  private final AtomicBoolean closed = new AtomicBoolean(false);
  private final AtomicBoolean more = new AtomicBoolean(false);

  private Context context;

  private ResultSet resultSet;

  private Handler<Throwable> exceptionHandler;
  private Handler<Row> handler;
  private Handler<Void> endHandler;

  private int fetchSize = 100; //default page sizes
  private boolean lastPage = false;
  private int currentRowIndex = 0;

  public CQLRowStreamImpl(Context context, ResultSet resultSet) {
    this.context = context;
    this.resultSet = resultSet;
    this.fetchSize = resultSet.getExecutionInfo().getStatement().getFetchSize();
  }

  @Override
  public CQLRowStream resultSetClosedHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public void moreResults() {
  }

  @Override
  public void close() {
    close(null);
  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    pause();
    if (closed.compareAndSet(false, true)) {
      if (handler != null) {
        handler.handle(Future.succeededFuture());
      }
    }
  }

  @Override
  public ReadStream<Row> exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }

  @Override
  public ReadStream<Row> handler(Handler<Row> handler) {
    this.handler = handler;
    resume();
    return this;
  }

  @Override
  public ReadStream<Row> pause() {
    paused.compareAndSet(false, true);
    return this;
  }

  @Override
  public ReadStream<Row> resume() {
    if (paused.compareAndSet(true, false)) {
      nextRow();
    }
    return this;
  }

  @Override
  public ReadStream<Row> endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    if (ended.compareAndSet(true, false)) {
      endHandler.handle(null);
    }
    return this;
  }

  private void nextRow() {
    if (!paused.get()) {
      for (Row row : resultSet) {
        handler.handle(row);
        currentRowIndex++;
      }
    }
    if (currentRowIndex < fetchSize - 1 && resultSet.getExecutionInfo().getPagingState() == null) {//must the end;
      if (ended.compareAndSet(false, true)) {
        endHandler.handle(null);
      }
    } else {
      if (resultSet.getExecutionInfo().getPagingState() != null) {// not the last page
        Futures.addCallback(resultSet.fetchMoreResults(), new FutureCallback<ResultSet>() {
          @Override
          public void onSuccess(ResultSet result) {
            resultSet = result;
            currentRowIndex = 0;
            nextRow();
          }

          @Override
          public void onFailure(Throwable t) {
            if (exceptionHandler != null) {
              exceptionHandler.handle(t);
            }
            close();
          }
        });
      }
    }
  }
}
