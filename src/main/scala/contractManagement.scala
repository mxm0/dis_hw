package db2
import java.sql.{ResultSet, SQLException}

object contractManagement {
  var input = 0
  var msg = ""

  def manageContracts() = {

    while(input != 4){
      showOptions()

      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)

      input match {
        case 1 =>
          var first_name = readLine("Insert first name: ")
          var name = readLine("Insert name: ")
          val address = readLine("Insert address: ")
          if(validatePersonInput(first_name, name, address)){
            var p = new Person(0, first_name, name, address)
            p.save()
            msg = "Person added"
          }
        case 2 =>
          // get input for contracts
          contractHelper.createContract()
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
