import java.sql.{Connection, DriverManager}

case class UserSchema(id: Int, email: String, productRefs: Seq[Int])

object WishlistDatabase {

  Class.forName("com.mysql.cj.jdbc.Driver")
// val jdbcHostname = "localhost" // si vas a correr localmente
  val jdbcHostname = "mysql" // si vas a correr en kubernetes
  // val jdbcHostname = "host.docker.internal" // usar esto si vas a correr con docker
  val jdbcPort = 3306
  val jdbcDatabase = "db-sist"
  val username = "user"
  val password = "pass"
  val jdbcUrl = s"jdbc:mysql://$username:$password@$jdbcHostname:$jdbcPort/$jdbcDatabase"

  val connection: Connection = DriverManager.getConnection(jdbcUrl)

  def setup(): Unit = {
    def setupUser = {
      println("Creating user table")
      connection.createStatement().execute(
        """
          |create table user (
          |  id                            bigint auto_increment not null,
          |  email                          varchar(255),
          |  constraint pk_user primary key (id)
          |);
        """.stripMargin)
      connection.createStatement().execute("insert into user (id,email) values (  1,'gonza@gmail.com');")
      connection.createStatement().execute("insert into user (id,email) values (  2,'apu@gmail.com');")
      connection.createStatement().execute("insert into user (id,email) values (  3,'marcos@gmail.com');")
      connection.createStatement().execute("insert into user (id,email) values (  4,'flor@gmail.com');")
      connection.createStatement().execute("insert into user (id,email) values (  5,'john@gmail.com');")
    }
    def setupProductUser = {
      println("Creating product_user table")
      connection.createStatement().execute(
        """
          |create table product_user (
          |  product_id                     bigint not null,
          |  user_id                        bigint not null,
          |  constraint pk_product_user primary key (product_id, user_id)
          |);
        """.stripMargin)
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (10, 1);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (10, 2);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (10, 3);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (10, 4);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (20, 1);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (20, 2);")
      connection.createStatement().execute("insert into product_user (product_id,user_id) values (30, 1);")
    }

    if (!tableExists) {
      setupUser
      setupProductUser
    }
  }

  def getAllUsers: List[UserSchema] = {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("SELECT * FROM user")
    var users: List[UserSchema] = Nil
    while (resultSet.next()) {
      val userId = resultSet.getInt("id")
      users =
        UserSchema(
          userId,
          resultSet.getString("email"),
          getProductsFromUser(userId)) :: users
    }
    users
  }

  def getUserById(id: Int): Option[UserSchema] = {
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(s"SELECT * FROM user WHERE id = $id")
    if (resultSet.next()) {
      val userId = resultSet.getInt("id")
      Some(UserSchema(
        userId,
        resultSet.getString("email"),
        getProductsFromUser(userId)))
    }
    else None
  }

  private def getProductsFromUser(idUser: Int): List[Int] = {
    var result: List[Int] = Nil
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(s"SELECT * FROM product_user WHERE user_id = $idUser")
    while (resultSet.next()) {
      result = resultSet.getInt("product_id") :: result
    }
    result
  }

  def addProductToUser(productId: Int, userId: Int): Boolean = {
    try {
      connection.createStatement().execute(s"insert into product_user (product_id,user_id) values ($productId,$userId);")
      true
    }
    catch{
      case e: Exception => false
    }
  }

  def deleteProductFromUser(productId: Int, userId: Int): Boolean = {
    try {
      connection.createStatement().execute(s"DELETE FROM product_user WHERE product_id = $productId AND user_id = $userId")
      true
    }
    catch{
      case e: Exception => false
    }
  }

  private def tableExists: Boolean = {
    val dbm = connection.getMetaData
    val tables = dbm.getTables(null, null, "user", null)
    tables.next()
  }
}
