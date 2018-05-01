package model

import java.sql.{SQLException, Statement}
import db2.ConnectionManager

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

 /*
  main entry point for creating properties
  incorporates both apt and houses.

  joins estate tables apt/house talbes
 */

}