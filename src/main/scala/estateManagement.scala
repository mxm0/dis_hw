package db2

object estateManagement {
  var input = 0

  def manageEstates() = {

    while(input != 5){
      showOptions()

      var arg = scala.io.StdIn.readLine()
      input = Main.toInt(arg)

      input match {
        case 1 => println("Login")
        case 2 => println("Create estate")
        case 3 => println("Update estate")
        case 4 => println("Delete estate")
        case 5 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def login() = {
  }

  def createEstate() = {
  // Connect to database
  }

  def updateEstate() = {
  }

  def deleteEstate() = {
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
