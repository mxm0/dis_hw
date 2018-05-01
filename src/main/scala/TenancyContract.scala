package db2

import java.sql.{SQLException, Date, Statement}

class TenancyContract(var Id: Int, var StartDate: Date, var Duration: String, var AdditionalCosts: String, var ContractId: Int) {
  def load() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select * from tenancy_contract where id = ?")
      pst.setInt (1, Id)
      var rs = pst.executeQuery()
      if(rs.next()){
        Id = rs.getInt(1)
        StartDate = rs.getDate(2)
        Duration = rs.getString(3)
        AdditionalCosts = rs.getString(4)
        ContractId = rs.getInt(5)
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
     var pst = db2.conn.prepareStatement("INSERT INTO TENANCY_CONTRACT(start_date, duration, additional_costs, contract_id) values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
     pst.setDate (1, StartDate)
     pst.setString (2, Duration)
     pst.setString (3, AdditionalCosts)
     pst.setInt (4, ContractId)
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
