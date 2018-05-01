package db2

import java.sql.{SQLException, Statement}

class Estate {

  private var _id : Int = -1
  private var _city : String = _
  private var _postalcode : String = _
  private var _street : String = _
  private var _street_number : String = _
  private var _square_area : String = _

  def id = _id
  def city = _city
  def postalcode = _postalcode
  def street = _street
  def street_number = _street_number
  def square_area = _square_area

  def id_= (value:Int):Unit = _id = value
  def city_= (value:String):Unit = _city = value
  def postalcode_= (value:String):Unit = _postalcode = value
  def street_= (value:String):Unit = _street = value
  def street_number_= (value:String):Unit = _street_number = value
  def square_area_= (value:String):Unit = _square_area = value

  override def toString: String = s"Estate(id=${_id}, city=${_city}, postalcode=${_postalcode}, " +
                                          s"street=${_street}, street_number=${_street_number}, " +
                                          s"square_area=${_square_area})"


  def load(id: Int): Estate = {
    try {
      val db2 = new ConnectionManager
      val selectSQL = "SELECT * FROM estate WHERE id = ?"
      val stmt = db2.conn.prepareStatement(selectSQL)
      stmt.setInt(1, id)
      val rs = stmt.executeQuery
      if (rs.next) {
        _id = id
        _city = rs.getString("city")
        _postalcode = rs.getString("postalcode")
        _street = rs.getString("street")
        _street_number = rs.getString("street_number")
        _square_area = rs.getString("square_area")
        rs.close()
        stmt.close()
        return this
      }
    } catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    null
  }

  def save(): Unit = {
    try {
      val db2 = new ConnectionManager
      if (_id == -1) {
        val query = """
                    |INSERT INTO estate
                    | (city, postalcode, street, street_number,square_area)
                    | VALUES (?, ?, ?, ?, ?)
                    """
        val pstmt = db2.conn.prepareStatement(query.stripMargin, Statement.RETURN_GENERATED_KEYS)
        pstmt.setString(1, _city)
        pstmt.setString(2, _postalcode)
        pstmt.setString(3, _street)
        pstmt.setString(4, _street_number)
        pstmt.setString(5, _square_area)
        pstmt.executeUpdate

        val rs = pstmt.getGeneratedKeys
        if (rs.next) _id = rs.getInt(1)
        rs.close()
        pstmt.close()
      }
      else {
        val updateSQL = """
                        |UPDATE estate
                        |SET city = ?, postalcode = ?, street = ?, street_number = ?, square_area = ?
                        |WHERE id = ?
                        """
        val pstmt = db2.conn.prepareStatement(updateSQL.stripMargin)
        pstmt.setString(1, _city)
        pstmt.setString(2, _postalcode)
        pstmt.setString(3, _street)
        pstmt.setString(4, _street_number)
        pstmt.setString(5, _square_area)
        pstmt.setInt(6, _id)
        pstmt.executeUpdate
        pstmt.close()
      }
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
  }
  /*
  estate_id_ = apt_res.getInt("estate_id")
  val query = """
                |select *
                |from manages
                |  inner join estate on manages.estate_id = estate.id
                |  inner join estate_agent on manages.estate_agent_id = estate_agent.id
                |  where estate.id = ?
              """
              .stripMargin
  val stmt = db2.conn.prepareStatement(query)
  stmt.setInt(1, estate_id_)

  val rs = stmt.executeQuery
  */

}