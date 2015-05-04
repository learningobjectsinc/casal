package com.learningobjects.casal

import net.liftweb.json.{JField, JObject}
import net.liftweb.json._

import scala.collection.immutable.HashMap

/**
 * Created by pgray on 3/6/15.
 */
case class Entity(
  identity: Identity,
  original: Option[AttributeSet],
  attributes: Option[AttributeSet],
  journal: List[AttributeSet]
)

case class Identity(
  originator_id: String,
  id: String
)

case class AttributeSet(
  uri: Option[String],
  share: Option[Boolean],
  propagate: Option[Boolean],
  timestamp: Option[String],
  use: Option[JObject],
  require: Option[JObject]
)

object CasaAttributes {
  def guids = Map(
    //foundational:
    "1f2625c2-615f-11e3-bf13-d231feb1dc81" -> "title",
    "b7856963-4078-4698-8e95-8feceafe78da" -> "description",
    "c4ed1e3c-9ed7-4355-b293-3ed2fbb4a5fb" -> "short_description",
    "c80df319-d5da-4f59-8ca3-c89b234c5055" -> "categories",
    "c6e33506-b170-475b-83e9-4ecd6b6dd42a" -> "tags",
    "d59e3a1f-c034-4309-a282-60228089194e" -> "authors",
    "273c148d-de83-499e-b554-4cac9b262ab6" -> "organization",

    //client
    "e730536a-22d0-4265-8aca-f1d2c23fafe0" -> "acceptable",
    "ef1c6344-5e2e-4dba-8fff-1638852694f8" -> "browser_features",
    "8d72d66c-0320-4861-8793-c5aebd195fc2" -> "android_app",
    "4439d4f9-3b62-4710-9535-ae3ebf885dac" -> "ios_app",
    "08099f0d-a38e-422a-8a0b-214d4808d7c8" -> "media_support",

    //interoperability:
    "f6820326-5ea3-4a02-840d-7f91e75eb01b" -> "lti",

    //privacy
    "63a39b88-2603-4bce-ac5b-8247bf262986" -> "privacy",
    "1d5b3bbe-5715-4064-adb8-65209eeda3fe" -> "privacy_url",

    //accessibility
    "40ece2ef-fd81-4e15-af82-146214f9e7a3" -> "accessibility_url",
    "221efedc-d6d7-4a79-bd6f-74f2efba4c67" -> "vpat_url",

    //random?
    "d25b3012-1832-4843-9ecf-3002d3434155" -> "icon"
  )
  def values = guids.map(_.swap)
}

object Casa {
  object Filters {
    def ADJINFILTER = (e:Entity) => {
      //does this entity have an attribute in 'required' that isn't in our supported casaAttributes map?
      e.original.fold(true)(_.require.fold(true)(_.obj.forall(
        CasaAttributes.values contains _.name
      )))
    }
  }
  object Transforms {
    def translateAttributes(j:Option[JObject])(implicit translations: Map[String, String] = Map.empty) = {
      val mergedTranslations = CasaAttributes.guids ++ translations
      j.map(j => JObject(
        j.obj.map(v => JField(mergedTranslations.getOrElse(v.name, v.name), v.value))
      ))
    }
    private def translateAttributeSet(a:Option[AttributeSet])(implicit translations: Map[String, String] = Map.empty) = {
      a.map(a => a.copy(
        use = translateAttributes(a.use),
        require = translateAttributes(a.require)
      ))
    }
    def ADJINTRANSLATE(e:Entity)(implicit translations: Map[String, String] = Map.empty) = {
      e.copy(
        original = translateAttributeSet(e.original),
        journal = e.journal.map(as => translateAttributeSet(Some(as)).getOrElse(null))
      )
    }

    def ADJINSQUASH(e:Entity) = {
      e.copy(attributes = e.journal.foldLeft(e.original)((l, r) => {
        l.map(l => {
          l.copy(
            use = Some(r.use.getOrElse(JObject(Nil)).merge(l.use.getOrElse(JObject(Nil)))),
            require = Some(r.require.getOrElse(JObject(Nil)).merge(l.require.getOrElse(JObject(Nil))))
          )
        })
      }))
    }
  }
}

//Model the known attribute objects

//TOOD: how to do snake casing in lift-json?
case class Lti(
  launch_url: Option[String],
  configuration_url: Option[String],
  registration_url: Option[String],
  versions_supported: Option[List[String]],
  outcomes: Option[JValue]
)