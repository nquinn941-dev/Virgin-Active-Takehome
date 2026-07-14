package com.quinn.virginactive.home.responses

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object HomeBlockSerializer : JsonContentPolymorphicSerializer<HomeBlock>(HomeBlock::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<HomeBlock> {
        val type = element.jsonObject["type"]?.jsonPrimitive?.contentOrNull

        return when (type) {
            "greeting" -> GreetingBlock.serializer()
            "hero" -> HeroBlock.serializer()
            "myClub" -> MyClubBlock.serializer()
            "classCarousel" -> ClassCarouselBlock.serializer()
            "myRewards" -> MyRewardsBlock.serializer()
            "myGoals" -> MyGoalsBlock.serializer()
            "promotion" -> PromotionBlock.serializer()
            "experimental" -> ExperimentalBlock.serializer()
            else -> UnknownBlockSerializer
        }
    }
}

object UnknownBlockSerializer : KSerializer<UnknownBlock> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("UnknownBlock")

    override fun serialize(
        encoder: Encoder,
        value: UnknownBlock
    ) {
        val output = encoder as? JsonEncoder ?: error("UnknownBlockSerializer only works with Json")
        output.encodeJsonElement(value.raw)
    }

    override fun deserialize(decoder: Decoder): UnknownBlock {
        val input = decoder as? JsonDecoder
            ?: error("UnknownBlockSerializer only works with Json")
        val element = input.decodeJsonElement().jsonObject
        val type = element["type"]?.jsonPrimitive?.contentOrNull ?: "unknown"
        return UnknownBlock(type = type, raw = element)
    }

}