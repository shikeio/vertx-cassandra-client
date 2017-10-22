package io.vertx.ext.cassandra;

import com.datastax.driver.core.ResultSet;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
// inspire by SQlRowStream
public interface CQLRowStream extends ReadStream<ResultSet> {

  CQLRowStream resultSetClosedHandler(Handler<Void> handler);

  void moreResults();

  void close();

  void close(Handler<AsyncResult<Void>> handler);

}
