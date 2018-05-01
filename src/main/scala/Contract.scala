package db2
import java.sql.{ResultSet, SQLException, Date, Statement}

class Contract(var Id: Int, var SignDate: Date, var PlaceId: String){
  def load() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select * from contract where id = ?")
      pst.setInt (1, Id)
      var rs = pst.executeQuery()
      if(rs.next()){
        Id = rs.getInt(1)
        SignDate = rs.getDate(2)
        PlaceId = rs.getString(3)
      }
        pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
    this
  }

  def save() = {
   val db2 = new ConnectionManager()
   try{
     var pst = db2.conn.prepareStatement("INSERT INTO CONTRACT(sign_date, place_id) values(?, ?)", Statement.RETURN_GENERATED_KEYS)
     pst.setDate (1, SignDate)
     pst.setString (2, PlaceId)
     pst.executeUpdate()
     val rs = pst.getGeneratedKeys
     if (rs.next) Id = rs.getInt(1)
     rs.close()
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
