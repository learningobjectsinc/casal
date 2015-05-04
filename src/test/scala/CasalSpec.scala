package com.learningobjects.casal

import java.util
import java.util.logging.Logger

import collection.mutable.Stack
import scala.io.Source
import org.scalatest._
import net.liftweb.json.{DefaultFormats, parse}

/**
 * Created by pgray on 5/1/15.
 */
class CasalSpec extends FlatSpec with Matchers {

  implicit val formats = DefaultFormats

  val log = Logger.getLogger(classOf[CasalSpec].getName)

  "A Entity" should "correctly model the CASA specification of an App" in {
    log.fine("reading & parsing example-entity.json")
    val json = Source.fromURL(getClass.getResource("/example-entity.json")).mkString
    val entity = parse(json).extract[Entity]
    val ident = entity.identity
    log.fine("parsed: " + ident.id + ":" + ident.originator_id)
  }

  "Casa.Transforms.ADJINTRANSLATE" should "take into account custom attributes" in {
    implicit val customCasaAttributes = Map("0edfbd3b-aced-4f19-8460-1952ac4f1588" -> "my_custom_guid")
    val json = Source.fromURL(getClass.getResource("/example-entity.json")).mkString
    val entity = parse(json).extract[Entity]
    val translated = Casa.Transforms.ADJINTRANSLATE(entity)
    assertResult("my_custom_value") {
      translated.original.get.use.get.values.get("my_custom_guid").get
    }
  }

  "Casa.Transforms.ADJINTRANSLATE" should "take into account specification attributes" in {
    val json = Source.fromURL(getClass.getResource("/example-entity.json")).mkString
    val entity = parse(json).extract[Entity]
    val translated = Casa.Transforms.ADJINTRANSLATE(entity)

    assertResult("Campus Pack") {
      translated.original.get.use.get.values.get("title").get
    }
  }


  "Casa.Transforms.ADJINTRANSLATE" should "override specification attributes with custom attributes" in {
    implicit val customCasaAttributes = Map("1f2625c2-615f-11e3-bf13-d231feb1dc81" -> "overriden_title")
    val json = Source.fromURL(getClass.getResource("/example-entity.json")).mkString
    val entity = parse(json).extract[Entity]
    val translated = Casa.Transforms.ADJINTRANSLATE(entity)

    assertResult("Campus Pack") {
      translated.original.get.use.get.values.get("overriden_title").get
    }
  }

}
