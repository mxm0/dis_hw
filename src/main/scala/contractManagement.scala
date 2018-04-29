object contractManagement {
  var input = 0

  def manageContracts() = {

    while(input != 4){
      showOptions()

      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)

      input match {
        case 1 => println("Insert person")
        case 2 => println("Create contract")
        case 3 => println("Show contracts")
        case 4 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def insertPerson(name: String, surname: String, address: String) = {
  // Connect to database
  }

  def createContract() = {
  }

  def showContracts() = {
  // SELECT * FROM CONTRACT TABLE
  // Print selected table
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
  }
}
