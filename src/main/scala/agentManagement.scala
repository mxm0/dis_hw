object agentManagement {
  var input = 0

  def manageAgents() = {
    
    while(input != 4){
      showOptions()
      
      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)
      
      input match {
        case 1 => println("Create account")
        case 2 => println("Edit account")
        case 3 => println("Delete account")
        case 4 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def createAccount() = {
  // Connect to database
  }

  def editAccount() = {
  }

  def deleteAccount() = {
  }
  
  def hashPwd() = {
  }

  def showOptions() = {
    print("\033[H\033[2J")
    print("AGENT MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Create account
            |2- Edit account
            |3- Delete account
            |4- Exit""".stripMargin)
  }

}
