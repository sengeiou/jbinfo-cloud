package cloud_

import scala.collection.convert.{DecorateAsJava, DecorateAsScala}

/**
  * @author xiaobin
  *         2017-10-30 下午7:49
  **/
object AllJavaConverters
  extends JavaConverters with JavaConvertersImplicits
    with DecorateAsJava with DecorateAsScala
