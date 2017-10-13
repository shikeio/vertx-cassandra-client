package io.vertx.ext.cassandra;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cassandra.impl.CassandraClientImpl;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
// wrapper a cassandra java-driver cluster
public interface CassandraClient {

  static CassandraClient createShared(Vertx vertx, JsonObject config) {
    return new CassandraClientImpl(vertx, config);
  }

  @Fluent
  CassandraClient connect(Handler<AsyncResult<CassandraSession>> handler);

  @Fluent
  CassandraClient connect(String keyspace, Handler<AsyncResult<CassandraSession>> handler);

  void close(Handler<AsyncResult<Void>> handler);
}
