package db2
import java.sql.{SQLException}

class Person(var first_name: String, var name: String, var address: String){
  def save() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("insert into Person (first_name, name, address) values(?, ?, ?, ?)")
      pst.setString (1, first_name)
      pst.setString (2, name)
      pst.setString (3, address)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
  }
}
