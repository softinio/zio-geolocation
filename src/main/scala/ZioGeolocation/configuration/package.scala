package ZioGeolocation

import zio.TaskR

package object configuration extends Configuration.Service[Configuration] {
  val load: TaskR[Configuration, Config] = TaskR.accessM(_.config.load)
}

