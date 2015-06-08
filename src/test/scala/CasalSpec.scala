package com.learningobjects.casal

import java.util.logging.Logger

import net.liftweb.json.{DefaultFormats, parse}
import org.scalatest._

import scala.io.Source

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

  "retrieve" should "retrieve entities from a url" in {
    val entities = Casa.retrieve("https://gist.githubusercontent.com/pfgray/f2d6b4414bdbb84bd75a/raw/960e5cadef6cc28cd1dd6610ff7f6c31b431ad00/lti.json")
    val translated = entities.map(Casa.Transforms.ADJINTRANSLATE)
  }

  "retrieveTranslate" should "retrieve entities from a url and translate them" in {
    val translated = Casa.retrieveTranslated("https://gist.githubusercontent.com/pfgray/f2d6b4414bdbb84bd75a/raw/960e5cadef6cc28cd1dd6610ff7f6c31b431ad00/lti.json")
  }

}
