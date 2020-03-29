package assign2

import org.apache.log4j.Logger
import parascale.actor.last.{Dispatcher, Task}
import parascale.util._
/**
 * Spawns a dispatcher to connect to multiple workers.
 */
object PerfectDispatcher extends App {
  val LOG = Logger.getLogger(getClass)
  LOG.info("started")

  // For initial testing on a single host, use this socket.
  // When deploying on multiple hosts, use the VM argument,
  // -Dsocket=<ip address>:9000 which points to the second
  // host.
  val socket2 = getPropertyOrElse("socket","localhost:9000")

  // Construction forks a thread which automatically runs the actor act method.
  new PerfectDispatcher(List("localhost:8000", socket2))
}

/**
 * Template dispatcher which tests readiness of
 * @param sockets
 */
class PerfectDispatcher(sockets: List[String]) extends Dispatcher(sockets) {
  import PerfectDispatcher._

  /**
   * Handles actor startup after construction.
   */
  def act: Unit = {
    LOG.info("sockets to workers = "+sockets)

    (0 until sockets.length).foreach { k =>
      LOG.info("sending message to worker " + k)
      workers(k) ! "to worker(" + k + ") hello from dispatcher"
    }

    // TODO: Replace the code below to implement PNF
    // Create the partition info and put it in two seaparate messages,
    // one for each worker, then wait below for two replies, one from
    // eah worker
    while (true)
    // This while loop wait forever but we really just need to wait
    // for two replies, one from each worker. The result, that is,
    // the partial sum and the elapsed times are in the payload as
    // a Result class.
      receive match {
        case task: Task if task.kind == Task.REPLY =>
          LOG.info("received reply " + task)
      }
  }
}
