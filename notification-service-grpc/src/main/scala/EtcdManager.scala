import java.nio.charset.Charset
import java.util.concurrent.{CompletableFuture, TimeUnit}

import akka.actor.ActorSystem
import io.etcd.jetcd.lock.LockResponse
import io.etcd.jetcd.options.PutOption
import io.etcd.jetcd.{ByteSequence, Client, KV}

import scala.concurrent.duration._


object EtcdManager  {
  val client: Client = Client.builder().endpoints("http://localhost:2379").build()
  val leaseClient = client.getLeaseClient
  val lockClient = client.getLockClient

  def registerInstanceInEtcd(port: Int, serviceName: String): Unit = {
    val client: Client = Client.builder().endpoints("http://localhost:2379").build()
    val kvClient: KV = client.getKVClient
    val key: ByteSequence = ByteSequence.from(s"services/$serviceName/$port".getBytes())

    val id = grantLease(3)
    println(s"making request to register service $serviceName in port $port")
    kvClient.put(key, ByteSequence.from(port.toString, Charset.forName("UTF-8")),PutOption.newBuilder().withLeaseId(id).build())
    /*
    * una vez que registras la key en etcd, tenes que hacer un keepAlive porque sino se baja despues de 3 segundos (el ttl).
    * Esto se usa para que el servicio no quede registrado cuando se cae.
    * */
    keepLeaseAlive(id)
  }


  def askForMaster(port: Int, ifGrantedMaster: () => Unit): CompletableFuture[LockResponse] = {
    val leaseID: Long = grantLease(3)
    keepLeaseAlive(leaseID)
    lockClient.lock(ByteSequence.from(s"notificationMaster".getBytes()),leaseID)
      .whenComplete((resp, throwable) => {
        if(throwable != null){
          println(s"error when asking for master")
          askForMaster(port, ifGrantedMaster)
        }
        else{
          println(s"port $port was assigned as master")
          ifGrantedMaster()
        }

      })
  }

  private def grantLease(ttl: Long): Long = {
    val feature = leaseClient.grant(ttl)
    val response = feature.get
    response.getID
  }

  private def keepLeaseAlive(id: Long): Unit = {
    val system = ActorSystem("mySystem")
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    system.scheduler.schedule(0 seconds, 2 seconds) {
      leaseClient.keepAliveOnce(id)
    }
  }
}