package db2

import java.sql.SQLException
import com.github.t3hnar.bcrypt._

class EstateAgent (var Id: Int = 0, var Name: String = "", var Address: String = "",
                   var Login: String = "", var Pwd: String = "") {

  override def toString: String = s"EstateAgent(id=$Id, name=$Name, Address=$Address," +
    s"Login=$Login, Pwd=$Pwd)"


  def createAccount() = {
    val hashedPwd = Pwd.bcrypt(generateSalt) // encrypt pwd
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("insert into estate_agent (name, address, login, password) values(?, ?, ?, ?)")
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
      db2.conn.close()
    }
  }


  def delete() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("delete from estate_agent where ? = login")
      pst.setString (1, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
  }

  def save() = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("update estate_agent set name = ?, address = ?, password  = ? where login = ?")
      val hashedPwd = Pwd.bcrypt(generateSalt) // encrypt pwd
      pst.setString (1, Name)
      pst.setString (2, Address)
      pst.setString (3, hashedPwd)
      pst.setString (4, Login)
      pst.executeUpdate()
      pst.close()
    } catch {
        case e: SQLException => {
          println(e.getMessage)
        }
    } finally {
      db2.conn.close()
    }
  }

  def pullAccount(): EstateAgent = {
    val db2 = new ConnectionManager()
    try{
      var pst = db2.conn.prepareStatement("select name, address, login, password, id from estate_agent where login = ?")
      pst.setString (1, Login)
      var rs = pst.executeQuery()
      if(rs.next()){
        Name = rs.getString(1)
        Address = rs.getString(2)
        Login = rs.getString(3)
        Pwd = rs.getString(4)
        Id = rs.getInt(5)
      }
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
}
