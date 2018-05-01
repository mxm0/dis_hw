package db2
import java.sql.{SQLException, Statement}

class Person(var Id: Int, var FirstName: String, var Name: String, var Address: String){
  def save() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("insert into Person (first_name, name, address) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
      pst.setString (1, FirstName)
      pst.setString (2, Name)
      pst.setString (3, Address)
      pst.executeUpdate()
      val rs = pst.getGeneratedKeys
      if (rs.next) Id = rs.getInt(1)
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
