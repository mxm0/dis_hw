package db2
import java.sql.{ResultSet, SQLException}

object contractManagement {
  var input = 0
  var msg = ""
  var estate_house_id = Map[Int, Int]()
  var estate_app_id = Map[Int, Int]()

  def manageContracts() = {

    while(input != 4){
      showOptions()

      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)

      input match {
        case 1 =>
          var first_name = readLine("Insert first name: ")
          var name = readLine("Insert name: ")
          var address = readLine("Insert address: ")
          if(validatePersonInput(first_name, name, address)){
            var p = new Person(0, first_name, name, address)
            p.save()
            msg = "Person added"
          }
        case 2 =>
          // Ask person details
          println("ENTER PERSON DETAILS")
          var first_name = readLine("Insert first name: ")
          var name = readLine("Insert name: ")
          var address = " "
          var p = new Person(0, first_name, name, address)
          
          // show all estates
          showEstates()
          // let user choose which estate to buy/sell
          var chosen_id = 0
          if(estate_house_id.values.exists(_ == None) || estate_app_id.values.exists(_ == None)){
            if(!validatePersonInput(first_name, name, address)){
              // Check if person is in the database otherwise create new person
              p = p.pull(first_name, name)
            }

            while(chosen_id == 0){
              var choice  = readLine("Insert estate id to rent/purchase: ")
              chosen_id = Main.toInt(choice)
            }
            var place = readLine("Insert signing place: ")
            // Create contract
            if(estate_house_id.contains(chosen_id)){
              // house
              var installment = readLine("Insert numbers of installments: ")
              var interest_rate = readLine("Insert interest rate: ")
              contractHelper.createSellContract(p.Id, estate_house_id(chosen_id), place, Main.toInt(installment), interest_rate)
              msg = "Sell contract created"
            }else{
              // appartment
              var start_date = readLine("Insert start date DD-MM-YYYY: ")
              var duration = readLine("Insert tenancy duration: ")
              var additional_costs = readLine("Insert additioncal costs: ")
              var df = new java.text.SimpleDateFormat("dd-MM-yyyy")
              var sqlDate = new java.sql.Date(df.parse(start_date).getTime())
              contractHelper.createRentContract(p.Id, estate_app_id(chosen_id), place, sqlDate, duration, additional_costs)
              msg = "Rent contract created"
            }
          }else{
            msg = "There are no available estates"
          }
        case 3 => 
          print("CONTRACTS OVERVIEW\n")
          contractHelper.showSellContracts()
          contractHelper.showRentContracts()
          var in = ""
          while(!in.equalsIgnoreCase("q")){
            in = readLine("Enter 'q/Q' to go back: ")
          }
        case 4 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

   def validatePersonInput(first_name: String, name: String, address: String): Boolean = {
     if(!first_name.matches("^[a-zA-Z]{1,255}$") || !name.matches("^[a-zA-Z]{1,255}$") ||
         !address.matches("^[a-zA-Z0-9]{1,255}$"))
       false
     else
       true
  }

  // THIS SHOULD BE IN ESTATE CLASS
  // The left join make sure that flats and house which are already sold/rented do not appear available
  def showEstates() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select * from estate inner join (SELECT * from house left join sells ON house.id = sells.house_id where sells.house_id is NULL) house on estate.id = house.estate_id")
      var rs = pst.executeQuery()
      printEstates(rs, true) 
      pst = db2.conn.prepareStatement("select * from estate inner join (SELECT * from appartment left join rents ON appartment.id = rents.appartment_id where rents.appartment_id is NULL) app on estate.id = app.estate_id")
      rs = pst.executeQuery()
      printEstates(rs, false) 
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

  def printEstates(rs: ResultSet, estate_type: Boolean) = {
    if(estate_type){
      while(rs.next()){
        estate_house_id += (rs.getInt(1) -> rs.getInt(7))
        println("ID: " + rs.getInt(1) + ", Location: " + rs.getString(2) + ", " + rs.getString(4) + " " + rs.getString(5) + ", " + rs.getString(3) + ", Square area: " + rs.getString(6) + ", Floors: " + rs.getString(8) + ", Price: " + rs.getString(9) + ",Garden: " + rs.getString(10))
      }
    }else{
      while(rs.next()){
        estate_app_id += (rs.getInt(1) -> rs.getInt(7))
        println("ID: " + rs.getInt(1) + ", Location: " + rs.getString(2) + ", " + rs.getString(4) + " " + rs.getString(5) + ", " + rs.getString(3) + ", Square area: " + rs.getString(6) + ", Floor: " + rs.getString(8) + ", Rent: " + rs.getString(9) + ", Rooms: " + rs.getString(10) + ", Balcony: " + rs.getString(11) + ", Buil-in-kitchen: " + rs.getString(12))
      }
    }
  }

  def showOptions() = {
    print("\033[H\033[2J")
    print("CONTRACT MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Insert person 
            |2- Create contract 
            |3- Show contracts
            |4- Exit""".stripMargin)
    if(!msg.isEmpty)
      println("\n" + msg + "\n")
  }
}
