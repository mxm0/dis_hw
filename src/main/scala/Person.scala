package db2
import java.sql.{SQLException, Statement}

class Person(var Id: Int, var FirstName: String, var Name: String, var Address: String){
  def save() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("insert into Person (first_name, name, address) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
      pst.setString (1, FirstName.toLowerCase)
      pst.setString (2, Name.toLowerCase)
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

  def pull(first_name: String, name: String): Person = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select * from person where first_name = ? and name = ?")
      pst.setString (1, first_name.toLowerCase)
      pst.setString (2, name.toLowerCase)
      var rs = pst.executeQuery()
      if(rs.next()){
        Id = rs.getInt(1)
        Address = rs.getString(4)
      }else{
        save()
        pst.close()
      }
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
    this
  }
}
