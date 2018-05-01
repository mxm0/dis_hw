package db2

class Person(var name: String, var surname: String, var address: String){
  def editAddress(addr: String) = {
    address = addr
  }
}
