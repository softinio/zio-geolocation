package ZioGeolocation

sealed trait ResultType
case object Simple extends ResultType
case object Full   extends ResultType
