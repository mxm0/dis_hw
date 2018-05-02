package db2

import java.sql.SQLException

import com.github.t3hnar.bcrypt._

object estateManagement {
  var input = 0
  var valid_login = false
  var account : EstateAgent = _

  def manageEstates() = {

    while(input != 5){
      showOptions()

      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)

      input match {
        case 1 =>
          val login = readLine("enter username: ")
          val estateAgent = new EstateAgent(Login = login)
          account = estateAgent.pullAccount()

          var pwd_in = ""
          val standardIn = System.console()
          if (standardIn != null) {
            println("Insert password: ")
            pwd_in = standardIn.readPassword().mkString
          }
          else {
            pwd_in = readLine("enter pwd: ")
          }
          if (pwd_in.isBcrypted(estateAgent.Pwd)) {
            valid_login = true
            println("\nSuccessful login!\n")
          }
          else println("\nUnsuccessful login. Please try again.\n")

        case 2 =>
          if (valid_login) createEstate(account)
          else println("\nPLEASE LOG IN.\n")
        case 3 =>
          if (valid_login) updateEstate(account)
          else println("\nPLEASE LOG IN.\n")
        case 4 =>
          if (valid_login) deleteEstate(account)
          else println("\nPLEASE LOG IN.\n")
        case 5 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def createEstate(agent : EstateAgent) = {
    var estate : Estate = new Estate
    estate.city = readLine("City ")
    estate.postalcode = readLine("Postal Code: ")
    estate.street = readLine("Street: ")
    estate.street_number = readLine("Street number: ")
    estate.square_area = readLine("Square Area: ")
    estate.save()

    val propType = readLine("Property type (house or apartment): ")
    if (propType == "house") {
      var house = new House
      house.floors = readLine("Number of floors: ")
      house.price = readLine("Price: ")
      house.garden = readLine("Garden: ")
      house.estate_id = estate.id
      house.save()
    }
    else if (propType == "apartment") {
      var apt = new Apartment
      apt.floors = readLine("Number of floors: ")
      apt.rooms = Main.toInt(readLine("Number of rooms: "))
      apt.price = readLine("Price: ")
      apt.balcony = Main.toInt(readLine("Balcony: "))
      apt.builtinkitchen = Main.toInt(readLine("Built in kitchen (0/1): "))
      apt.estate_id = estate.id
      apt.save()
    }
    else println("\nUnmanaged property type\n")

    try {
      val query = """
                    |insert into manages(estate_agent_id, estate_id)
                    |values (?, ?)
                  """
      val db2 = new ConnectionManager
      val stmt = db2.conn.prepareStatement(query.stripMargin)
      stmt.setInt(1, agent.Id)
      stmt.setInt(2, estate.id)
      stmt.executeUpdate
      stmt.close()
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
  }

  def updateEstate(agent : EstateAgent) = {
    try {
      val db2 = new ConnectionManager
      var estate : Estate = new Estate
      val estate_id = Main.toInt(readLine("Enter estate ID: "))

      val query = """
                    | select *
                    | from manages
                    | where estate_id = ?
                    | and estate_agent_id = ?
                  """
      val stmt = db2.conn.prepareStatement(query.stripMargin)
      stmt.setInt(1, estate_id)
      stmt.setInt(2, agent.Id)
      var rs = stmt.executeQuery()
      if (rs.next) {
        estate.load(estate_id)
        estate.city = readLine("City ")
        estate.postalcode = readLine("Postal Code: ")
        estate.street = readLine("Street: ")
        estate.street_number = readLine("Street number: ")
        estate.square_area = readLine("Square Area: ")
        estate.save()
      }
      else println("\nYou do not manage this property. Sorry.\n")
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
  }

  def deleteEstate(agent : EstateAgent) = {
    try {
      val db2 = new ConnectionManager
      var estate : Estate = new Estate
      val estate_id = Main.toInt(readLine("Enter estate ID: "))

      val query = """
                    | select *
                    | from manages
                    | where estate_id = ?
                    | and estate_agent_id = ?
                  """
      val stmt = db2.conn.prepareStatement(query.stripMargin)
      stmt.setInt(1, estate_id)
      stmt.setInt(2, agent.Id)
      var rs = stmt.executeQuery()
      if (rs.next) {
        estate.load(estate_id)
        estate.delete()
      }
      else println("\nYou do not manage this property. Sorry.\n")
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
  }

  def showOptions() = {
    print("\033[H\033[2J")
    print("ESTATES MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Login
            |2- Create estate
            |3- Update estate
            |4- Delete estate
            |5- Exit""".stripMargin)
  }
}
