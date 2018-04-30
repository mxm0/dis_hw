import java.sql.{ResultSet, SQLException}
import com.github.t3hnar.bcrypt._

class EstateAgent (var Name: String, var Address: String, var Login: String, var Pwd: String) {

  def createAccount() = {
    val hashedPwd = Pwd.bcrypt(generateSalt) // encrypt pwd
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("insert into estate_agent (name, address, login, password) values(?, ?, ?, ?)")
      pst.setString (1, Name)
      pst.setString (2, Address)
      pst.setString (3, Login)
      pst.setString (4, hashedPwd)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }
  }

  def changeName() = {
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("update estate_agent set name = ? where login = ?")
      pst.setString (1, Name)
      pst.setString (2, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }

  }

    def changeAddress() = {
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("update estate_agent set address = ? where login = ?")
      pst.setString (1, Address)
      pst.setString (2, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }

  }

  def changePwd() = {
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("update estate_agent set password  = ? where login = ?")
      val hashedPwd = Pwd.bcrypt(generateSalt) // encrypt pwd
      pst.setString (1, hashedPwd)
      pst.setString (2, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }
  }

  def deleteAccount() = {
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("delete from estate_agent where ? = login")
      pst.setString (1, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }
  }

  def pullAccount(): EstateAgent = {
    val conn: java.sql.Connection = Main.db2_connect()
    try{
      var pst = conn.prepareStatement("select name, address, login from estate_agent where login = ?")
      pst.setString (1, Login)
      var rs = pst.executeQuery()
      if(rs.next()){
        Name = rs.getString(1)
        Address = rs.getString(2)
        Login = rs.getString(3)
      }
        pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      conn.close()
    }
    this
  }
}
