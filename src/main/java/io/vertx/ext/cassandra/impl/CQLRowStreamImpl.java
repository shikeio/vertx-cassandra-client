package io.vertx.ext.cassandra.impl;

import com.datastax.driver.core.ResultSet;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.cassandra.CQLRowStream;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class CQLRowStreamImpl implements CQLRowStream {

  @Override
  public CQLRowStream resultSetClosedHandler(Handler<Void> handler) {
    return null;
  }

  @Override
  public void moreResults() {

  }

  @Override
  public void close() {

  }

  @Override
  public void close(Handler<AsyncResult<Void>> handler) {

  }

  @Override
  public ReadStream<ResultSet> exceptionHandler(Handler<Throwable> handler) {
    return null;
  }

  @Override
  public ReadStream<ResultSet> handler(Handler<ResultSet> handler) {
    return null;
  }

  @Override
  public ReadStream<ResultSet> pause() {
    return null;
  }

  @Override
  public ReadStream<ResultSet> resume() {
    return null;
  }

  @Override
  public ReadStream<ResultSet> endHandler(Handler<Void> endHandler) {
    return null;
  }
}
