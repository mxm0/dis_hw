import com.github.t3hnar.bcrypt._
import java.sql.{ResultSet, SQLException}

object agentManagement {

  def manageAgents() = {
    
    var input = 0
    var msg = ""

    while(input != 4){
      showMainOptions(msg)
      
      var arg = readLine()
      input = Main.toInt(arg)
      
      input match {
        case 1 =>
          // Ask for account details
          val name = readLine("Insert name: ")
          val address = readLine("Insert address: ")
          val login = readLine("Insert login username: ")
          // Get password
          val standardIn = System.console()
          println("Insert password: ")
          val pwd_in = standardIn.readPassword()
          if(validateInput(name, address, login, pwd_in.mkString)){
            var estateAgent = new EstateAgent(name, address, login, pwd_in.mkString)
            estateAgent.createAccount()
            msg = "Account created"
          }else{
            println("Input error")
          }

        case 2 => 
           val login = readLine("Insert login of account to edit: ")
           editAccount(login)

        case 3 =>
          val login = readLine("Insert login username: ")
          var estateAgent = new EstateAgent("", "", login, "")
          estateAgent.deleteAccount()
          msg = "Account deleted"
        
        case 4 => print("\033[H\033[2J")
        
        case whoa =>
          print("\033[H\033[2J")
          println("Chosen action is not listed\n")
      }
    }
  }

  def editAccount(login: String) = {
   var estateAgent = new EstateAgent("", "", login, "")
   estateAgent = estateAgent.pullAccount()
   var input = 0  
   while(input !=4){
     showEditOptions()
     println("\nName: " + estateAgent.Name + "\nAddress: " + estateAgent.Address + "\nLogin: " + estateAgent.Login + "\n") 
     var arg = readLine()
     input = Main.toInt(arg)
     input match {
       case 1 =>
         var new_name = readLine("Insert new name: ")
         estateAgent.Name = new_name
         estateAgent.changeName()

       case 2 =>
         var new_address = readLine("Insert new address: ")
         estateAgent.Address = new_address
         estateAgent.changeAddress()

       case 3 =>
         val standardIn = System.console()
         println("Insert new password: ")
         var new_pwd = standardIn.readPassword()
         estateAgent.changePwd()
         
       case 4 =>
         print("\033[H\033[2J")

       case whoa => 
            print("\033[H\033[2J")
            println("Chosen action is not listed\n")

     }
   }
  }

  def validateInput(name: String, address: String, login: String, pwd:String): Boolean = {
    if(!name.matches("^[a-zA-Z]{1,255}$") || !address.matches("^[a-zA-Z0-9]{1,255}$") ||
        !login.matches("^[a-zA-Z0-9]{1,40}$") || !pwd.matches("^(.*?){1,60}$"))
      false
    else
      true
  }

  def showMainOptions(msg: String) = {
    print("\033[H\033[2J")
    print("AGENT MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Create account
            |2- Edit account
            |3- Delete account
            |4- Exit""".stripMargin)
    println("\n" + msg + "\n")
  }

  def showEditOptions() = {
    print("\033[H\033[2J")
    print("AGENT MANAGEMENT MODE")
    println("""
            |----------------------
            |1- Change name 
            |2- Change address
            |3- Change password
            |4- Back""".stripMargin)

  }
}
