package model

import db2.ConnectionManager
import java.sql.{SQLException, Statement}

class Apartment {

  private var _id = -1
  private var _estate_id = -1
  private var _floors: String = _
  private var _price: String = _
  private var _garden: String = _
  private var _rooms: Int = _
  private var _balcony: Int = _
  private var _builtinkitchen: Int = _

  def id = _estate_id
  def estate_id = _estate_id
  def floors = _floors
  def price = _price
  def garden = _garden
  def rooms = _rooms
  def balcony = _balcony
  def builtinkitchen = _builtinkitchen

  def id_= (value:Int):Unit = _id = value
  def estate_id_= (value:Int):Unit = _estate_id = value
  def floors_= (value:String):Unit = _floors = value
  def price_= (value:String):Unit = _price = value
  def garden_= (value:String):Unit = _garden = value
  def rooms_= (value:Int):Unit = _rooms = value
  def balcony_= (value:Int):Unit = _balcony = value
  def builtinkitchen_= (value:Int):Unit = _builtinkitchen = value

  override def toString: String = s"Apartment(id=${_id}, estate_id=${_estate_id}, floors=${_floors}, " +
                                            s"price=${_price}, garden=${_garden}, rooms=${_rooms}" +
                                            s"builtinkitchen=${_builtinkitchen}, balcony=${_balcony})"

  def load(id: Int): Apartment = {
    try {
      val db2 = new ConnectionManager
      val stmt = db2.conn.prepareStatement("select * from appartment where id = ?")
      stmt.setInt(1, id)
      val apt_res = stmt.executeQuery
      if (apt_res.next) {
        _id = id
        _estate_id = apt_res.getInt("estate_id")
        _floors = apt_res.getString("floors")
        _price = apt_res.getString("price")
        _garden = apt_res.getString("garden")
        _rooms = apt_res.getInt("rooms")
        _balcony = apt_res.getInt("balcony")
        _builtinkitchen = apt_res.getInt("builtinkitchen")
        apt_res.close()
        stmt.close()
        return this
      }
    }
    catch {
      case e: SQLException =>
        e.printStackTrace()
    }
    null
  }

  def save(): Unit = {
    val db2 = new ConnectionManager
    try {
      if (_id == -1) {
        // wenn das Objekt noch keine ID hat
        val insertSQL =
          """
            |INSERT INTO
            |appartment(floors, garden, price, rooms, balcony, builtinkitchen, estate_id)
            |VALUES (?, ?, ?, ?, ?, ?, ?)
          """
        val stmt = db2.conn.prepareStatement(insertSQL.stripMargin, Statement.RETURN_GENERATED_KEYS)
        stmt.setString(1, _floors)
        stmt.setString(2, _garden)
        stmt.setString(3, _price)
        stmt.setInt(4, _rooms)
        stmt.setInt(5, _balcony)
        stmt.setInt(6, _builtinkitchen)
        stmt.setInt(7, _estate_id)
        stmt.executeUpdate

        val rs = stmt.getGeneratedKeys
        if (rs.next) _id = rs.getInt(1)
        rs.close()
        stmt.close()
      }
      else {
        // Falls schon eine ID vorhanden ist, mach ein Update...
        val updateSQL =
          """
            |UPDATE appartment
            |SET floors = ?, garden = ?, price = ?, estate_id = ?, rooms = ?,
            |    balcony = ?, builtinkitchen = ?
            |WHERE id = ?
          """
        val pstmt = db2.conn.prepareStatement(updateSQL.stripMargin)
        pstmt.setString(1, _floors)
        pstmt.setString(2, _garden)
        pstmt.setString(3, _price)
        pstmt.setInt(4, _estate_id)
        pstmt.setInt(5, _rooms)
        pstmt.setInt(5, _balcony)
        pstmt.setInt(5, _builtinkitchen)
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
}