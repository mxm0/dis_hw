package db2

import java.sql.{ResultSet, SQLException, Statement}

class PurchaseContract(var Id: Int, var Installment: Int, var InterestRate: String, var ContractId: Int) {
  
  def load() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select * from purchase_contract where id = ?")
      pst.setInt (1, Id)
      var rs = pst.executeQuery()
      if(rs.next()){
        Id = rs.getInt(1)
        Installment = rs.getInt(2)
        InterestRate = rs.getString(3)
        ContractId = rs.getInt(4)
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
     var pst = db2.conn.prepareStatement("INSERT INTO PURCHASE_CONTRACT(installment, interest_rate, contract_id) values(?, ?, ?)", Statement.RETURN_GENERATED_KEYS)
     pst.setInt (1, Installment)
     pst.setString (2, InterestRate)
     pst.setInt (3, ContractId)
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
