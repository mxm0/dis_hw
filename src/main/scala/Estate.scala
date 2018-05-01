package model

import java.sql.{SQLException, Statement}
import db2.ConnectionManager

class Estate {

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