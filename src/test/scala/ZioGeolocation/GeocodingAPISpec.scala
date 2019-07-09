package ZioGeolocation

import org.specs2.Specification

class HelloSpec extends Specification {
  def is = "HelloSpec".title ^ s2"""
    The Hello object returns say Hello $e1 
    """
  def e1 =
    Hello.greeting must_=== "hello"
}
