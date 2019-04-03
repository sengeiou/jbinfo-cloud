package cloud_.converter

case class IntegerAsScala(i: java.lang.Integer) {
  def asScala: Option[Int] = Option(i).map(_.asInstanceOf[Int])
}
