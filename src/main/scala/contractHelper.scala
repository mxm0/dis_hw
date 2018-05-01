package db2
import java.sql.{ResultSet, SQLException, Date}

object contractHelper {
  def showSellContracts() = {
    // I KNOW.... DON'T JUDGE ME
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("SELECT person.first_name, estate.city, estate.postalcode, estate.price, contract.contract_id, contract.installment, contract.interest_rate FROM sells inner join (SELECT estate.city, estate.postalcode, house.id as house_id, house.price from estate inner join house on estate.id = house.estate_id) estate on sells.house_id = estate.house_id inner join person on sells.person_id = person.id INNER JOIN (SELECT contract.sign_date, contract.place_id, contract.id as contract_id, purchase_contract.id as purchase_id, purchase_contract.installment, purchase_contract.interest_rate from contract INNER JOIN purchase_contract ON contract.id = purchase_contract.contract_id) contract on sells.purchase_contract_id = contract.purchase_id")
      var rs = pst.executeQuery()
      printResult(rs, true)
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
  }

  def showRentContracts() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("SELECT person.first_name, estate.city, estate.postalcode, estate.price, contract.contract_id, contract.start_date, contract.duration FROM rents inner join (SELECT estate.city, estate.postalcode, appartment.id as appartment_id, appartment.price from estate inner join appartment on estate.id = appartment.estate_id) estate on rents.appartment_id = estate.appartment_id inner join person on rents.person_id = person.id INNER JOIN (SELECT contract.sign_date, contract.place_id, contract.id as contract_id, tenancy_contract.id as tenancy_id, tenancy_contract.start_date, tenancy_contract.duration from contract INNER JOIN tenancy_contract ON contract.id = tenancy_contract.contract_id) contract on rents.tenancy_contract_id = contract.tenancy_id")
      var rs = pst.executeQuery()
      printResult(rs, false)
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
  }

  def printResult(rs: ResultSet, estate: Boolean) = {
    if(estate)
      while(rs.next()){
        println("Contract no: " + rs.getInt(5) + ", First name: " + rs.getString(1) + ", House location: " + rs.getString(2) + ", ZIP:" + rs.getString(3) + ", Price: " + rs.getString(4) + ", Purchase installment: " + rs.getInt(6) + ", Interest rate: " + rs.getString(7))
    }else{
      while(rs.next()){
        println("Contract no: " + rs.getInt(5) + ", First name: " + rs.getString(1) + ", House location: " + rs.getString(2) + ", ZIP:" + rs.getString(3) + ", Rent: " + rs.getString(4) + ", Start data: " + rs.getString(6) + ", Duration: " + rs.getString(7))
      }
    }
  }
  
  def createRentContract(personId: Int, app_id: Int, place: String, StartDate: Date, Duration: String, AdditionalCosts: String) = {
    // Make new contract
    var date = new java.sql.Date(new java.util.Date().getTime());
    var contract = new Contract(0, date, place)
    contract.save()

    // Make new tenancy contract
    var tenancy_contract = new TenancyContract(0, StartDate, Duration, AdditionalCosts, contract.Id)
    tenancy_contract.save()

    // Save new contract in rent
    val db2 = new ConnectionManager()
      try{
      var pst = db2.conn.prepareStatement("INSERT INTO RENTS(tenancy_contract_id, appartment_id, person_id) values(?, ?, ?)")
      pst.setInt (1, tenancy_contract.Id)
      pst.setInt (2, app_id)
      pst.setInt (3, personId)
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

  def createSellContract(personId: Int, house_id: Int, place: String, Installment: Int, InterestRate: String) = {
    // Make new contract
    var date = new java.sql.Date(new java.util.Date().getTime());
    var contract = new Contract(0, date, place)
    contract.save()

    // Make new tenancy contract
    var purchase_contract = new PurchaseContract(0, Installment, InterestRate, contract.Id)
    purchase_contract.save()

    // Save new contract in rent
    val db2 = new ConnectionManager()
      try{
      var pst = db2.conn.prepareStatement("INSERT INTO SELLS(purchase_contract_id, house_id, person_id) values(?, ?, ?)")
      pst.setInt (1, purchase_contract.Id)
      pst.setInt (2, house_id)
      pst.setInt (3, personId)
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
