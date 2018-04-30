package db2

import java.sql.{ResultSet, DriverManager, Statement}

class ConnectionManager {

  // these should live in a config file, oh well
  private val url : String  = "jdbc:db2://vsisls4.informatik.uni-hamburg.de:50001/VSISP"
  private val user : String  = "vsisp01"
  private val pw : String = "aqMRJ4VO"

  // initialize JDBC driver & connection pool
  Class.forName("com.ibm.db2.jcc.DB2Driver")
  val conn : java.sql.Connection = DriverManager getConnection(url, user, pw)

  // switch direct use of "conn" to loaner pattern (gist.github.com/aldonline/6959105)
  def select(str : String) : Stream[ResultSet] = withStatement { s =>
    val rs = conn prepareStatement str executeQuery()
    new Iterator[ResultSet] { def hasNext = rs.next ;  def next = rs }.toStream
  }

  // loan a sql statement (i.e., curry the statement creation)
  def withStatement[R]( f:(Statement) => R ): R =
    withConn { c => f( c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY) ) }

  // loan a sql connection (i.e., curry the connection creation/deletion)
  def withConn[R]( f:(java.sql.Connection) => R ):R = {
    val conn = DriverManager getConnection(url, user, pw)
    try f(conn) finally conn.close()
  }
}