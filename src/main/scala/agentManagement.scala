//import scalikejdbc._
import com.github.t3hnar.bcrypt._

object agentManagement {
  var input = 0

  def manageAgents() = {
    
    while(input != 4){
      showOptions()
      
      var arg = readLine()
      input = Main.toInt(arg)
      
      input match {
        case 1 =>
          // NEED TO VALIDATE USER INPUT FOR MIN/MAX LENGTH
          // Ask for account details
          val name = readLine("Insert name: ")
          val address = readLine("Insert address: ")
          val login = readLine("Insert login username: ")
          // Get password
          val standardIn = System.console()
          println("Insert password: ")
          val pwd_in = standardIn.readPassword()

          createAccount(name, address, login, pwd_in.mkString)
        case 2 => println("Edit account")
        case 3 => println("Delete account")
        case 4 => print("\033[H\033[2J")
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def createAccount(name: String, address: String, login: String, pwd: String) = { 
    val hashedPwd = pwd.bcrypt(generateSalt)
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("insert into estate_agent (name, address, login, password) values(?, ?, ?, ?)")
      pst.setString (1, name)
      pst.setString (2, address)
      pst.setString (3, login)
      pst.setString (4, hashedPwd)
      pst.executeUpdate()
      pst.close()
    } finally {
      conn.close()
    }
  }

  def editAccount() = {
  }

  def deleteAccount() = {
  }
  
  def showOptions() = {
    //print("\033[H\033[2J")
    print("AGENT MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Create account
            |2- Edit account
            |3- Delete account
            |4- Exit""".stripMargin)
  }

}
