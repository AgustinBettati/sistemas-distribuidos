import example.hello.{HelloReply, HelloRequest, HelloServiceGrpc}
import generated.product_service.{ProductList, ProductRequest, ProductServiceGrpc, ProductsRequest}
import generated.user.{ProductUserRequest, UserRequest, UserServiceGrpc, UsersRequest}
import io.grpc.{ManagedChannelBuilder, ServerBuilder}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}


object ClientDemo extends App {

  implicit val ec = ExecutionContext.global

  val channel = ManagedChannelBuilder.forAddress("localhost", 9000)
    .usePlaintext(true)
    .build()


  val blockingStub = ProductServiceGrpc.blockingStub(channel)
  val stub = ProductServiceGrpc.stub(channel)

  val channelUser = ManagedChannelBuilder.forAddress("localhost", 8000)
    .usePlaintext(true)
    .build()
  val blockingStubUser = UserServiceGrpc.blockingStub(channelUser)
  val stubUser = UserServiceGrpc.stub(channelUser)

  val users = stubUser.getUsers(UsersRequest())

  users.onComplete {
    case Success(value) => {
      value.users.foreach(u =>
        print(u.name + "\t\t" + u.productReferences.get.id + "\n")
      )

      val products: Future[ProductList] = stub.getProducts(ProductsRequest())
      products.onComplete {
        case Success(value) => {
          for (elem <- value.product) {
            print(elem.id + " ----> " + elem.name + " , " + elem.description + "\n")
          }
          print("\n\n Adding product 1 to flor...\n")
          stubUser.addProduct(ProductUserRequest(40, 1)).onComplete {
            case Success(value) => {
              stubUser.getUser(UserRequest(40)).onComplete {
                case Success(value) => {
                  print(value.name + "\t\t" + value.productReferences.get.id + "\n")
                }
                case Failure(exception) => print(exception)
              }

            }
            case Failure(exception) => print(exception)
          }
        }
        case Failure(exception) => print(exception)
      }
    }


    case Failure(exception)
    => print(exception)
  }


  //  val product1=stub.getProduct(ProductRequest(1))
  //  product1.onComplete {
  //    case Success(value) => print(value.name+ "\n")
  //    case Failure(exception) => print(exception)
  //  }


  //  print("ADD PRODUCT ")

  //  stubUser.addProduct()
  //  val start = System.currentTimeMillis()
  //
  //  val futures: immutable.Seq[Future[HelloReply]] = for (i <- 1 to 10000) yield
  //     stub.sayHello(HelloRequest("Juan"))
  //
  //
  //  val result: Future[immutable.Seq[HelloReply]] = Future.sequence(futures)
  //
  //  result.onComplete { r =>
  //    val end = System.currentTimeMillis()
  //
  //    println(end - start)
  //
  //    println("Completed")
  //  }
  //
  System.in.read()
}