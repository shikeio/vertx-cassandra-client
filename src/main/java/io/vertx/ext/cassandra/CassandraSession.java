package io.vertx.ext.cassandra;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;

import java.util.Map;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
//wrapper a cassandra java-driver session
public interface CassandraSession {

  String getLoggedKeyspace();

  @Fluent
  CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler);

  @Fluent
  CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler, Object... values);

  @Fluent
  CassandraSession execute(String query, Handler<AsyncResult<ResultSet>> handler, Map<String, Object> values);

  @Fluent
  CassandraSession execute(Statement statement, Handler<AsyncResult<ResultSet>> handler);

  @Fluent
  CassandraSession prepare(String query, Handler<AsyncResult<PreparedStatement>> handler);

  @Fluent
  CassandraSession prepare(RegularStatement statement, Handler<AsyncResult<PreparedStatement>> handler);

  void close(Handler<AsyncResult<Void>> handler);
}
