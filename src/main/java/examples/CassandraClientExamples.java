package examples;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.cassandra.CassandraClient;

/**
 * @author Ranger Tsao(https://github.com/boliza)
 */
public class CassandraClientExamples {

  public static void main(String[] args) {
    CassandraClient client = CassandraClient.createShared(Vertx.vertx(), new JsonObject());
    client.connect(event -> {
      if (event.succeeded()) {
        event.result().execute("CREATE KEYSPACE IF NOT EXISTS nidaye WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};",
                               createH -> {
                                 if (createH.succeeded()) {
                                   System.out.printf("niubi");
                                 } else {
                                   System.out.println(createH.cause());
                                 }
                               });
      }
    });
  }

}
