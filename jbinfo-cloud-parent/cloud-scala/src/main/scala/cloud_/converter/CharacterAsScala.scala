package cloud_.converter

case class CharacterAsScala(c: java.lang.Character) {
  def asScala: Option[Char] = Option(c).map(_.asInstanceOf[Char])
}
