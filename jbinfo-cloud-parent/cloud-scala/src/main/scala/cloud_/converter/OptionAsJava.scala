package cloud_.converter

import java.util.Optional

import cloud_.JavaConverters


/**
  * @author xiaobin
  *         2017-10-30 下午7:46
  **/
case class OptionAsJava[A](opt: Option[A]) {
  def asJava: Optional[A] = JavaConverters.toJavaOptional(opt)
}

