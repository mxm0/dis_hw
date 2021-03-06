package db2

import java.sql.{Connection, ResultSet, SQLException}

object Main extends App {
  var input = 0 
  val pwd = "toor"
  var err = false

  while(input != 4){
    if(err){
      println("Pwd incorrect\n")
      err = false
    }
    
    print("MAIN MENU")
    println("""
           |----------------------  
           |1- Manage estate agents
           |2- Manage estates
           |3- Manage contracts
           |4- Quit""".stripMargin)
    
    
    var arg = readLine()

    input = toInt(arg)
 
    input match {
         case 1 => 
           println("Insert password...")   
           val standardIn = System.console()
           val pwd_in = standardIn.readPassword()
           if(pwd == pwd_in.mkString)
             agentManagement.manageAgents()
           else{
             print("\033[H\033[2J")
             err = true 
           }
         case 2 => 
           estateManagement.manageEstates()
         case 3 => contractManagement.manageContracts()
         case 4 => println("Quitting app...")
         case whoa =>  
           print("\033[H\033[2J")
           println("Chosen action is not listed\n")
    }
  }

  def toInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case e: Exception => 0
    }
  }

  def db2_connect(): java.sql.Connection = {
    // initialize JDBC driver & connection pool
    Class.forName("com.ibm.db2.jcc.DB2Driver")
      var connection = java.sql.DriverManager.getConnection("jdbc:db2://localhost:50000/VSISP", "vsisp", "") 
      connection
  }
}
