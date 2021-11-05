package com.rarible.protocol.union.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger

class UnionJacksonTest {

    private val mapper = ObjectMapper()
        .registerModule(UnionPrimitivesJacksonModule)
        .registerModule(UnionModelJacksonModule)
        .registerModule(KotlinModule())

    private val legacyMapper = ObjectMapper()
        .registerModule(KotlinModule())

    @Test
    fun `serialize big decimal with trailing zeros`() {
        val expected = BigDecimal("0.00000000013")
        val value = BigDecimal("0.000000000130000000")

        val serialized = mapper.writeValueAsString(value)
        assertEquals("\"0.00000000013\"", serialized)

        val deserialized = mapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserialized, expected)

        val deserializedLegacy = legacyMapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserializedLegacy, expected)
    }

    @Test
    fun `serialize zero big decimal with trailing zeros`() {
        val expected = BigDecimal.ZERO
        val value = BigDecimal("0.000000000")

        val serialized = mapper.writeValueAsString(value)
        assertEquals("\"0\"", serialized)

        val deserialized = mapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserialized, expected)

        val deserializedLegacy = legacyMapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserializedLegacy, expected)
    }

    @Test
    fun `serialize big decimal`() {
        val value = BigDecimal("1.3E-10")

        val serialized = mapper.writeValueAsString(value)
        assertEquals("\"0.00000000013\"", serialized)

        val deserialized = mapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserialized, value)

        val deserializedLegacy = legacyMapper.readValue(serialized, BigDecimal::class.java)
        assertEquals(deserializedLegacy, value)
    }

    @Test
    fun `serialize big int`() {
        val value = BigInteger("10000000000000000000000000000000000000000000000000000000000")
        val serialized = mapper.writeValueAsString(value)
        assertEquals("\"10000000000000000000000000000000000000000000000000000000000\"", serialized)

        val deserialized = mapper.readValue(serialized, BigInteger::class.java)
        assertEquals(deserialized, value)

        val deserializedLegacy = legacyMapper.readValue(serialized, BigInteger::class.java)
        assertEquals(deserializedLegacy, value)
    }

    @Test
    fun `eth address`() {
        val ethAddress = UnionAddress(BlockchainDto.ETHEREUM, "123")

        val serialized = mapper.writeValueAsString(ethAddress)
        assertEquals("\"ETHEREUM:123\"", serialized)

        val deserialized = mapper.readValue(serialized, UnionAddress::class.java)
        assertEquals(ethAddress, deserialized)
    }

    @Test
    fun `flow address`() {
        val flowAddress = UnionAddress(BlockchainDto.FLOW, "123")

        val serialized = mapper.writeValueAsString(flowAddress)
        assertEquals("\"FLOW:123\"", serialized)

        val deserialized = mapper.readValue(serialized, UnionAddress::class.java)
        assertEquals(flowAddress, deserialized)
    }

    @Test
    fun `flow contract`() {
        val flowContract = UnionAddress(BlockchainDto.FLOW, "123abc")

        val serialized = mapper.writeValueAsString(flowContract)
        assertEquals("\"FLOW:123abc\"", serialized)

        val deserialized = mapper.readValue(serialized, UnionAddress::class.java)
        assertEquals(flowContract, deserialized)
    }

    @Test
    fun `eth itemId`() {
        val itemId = ItemIdDto(
            blockchain = BlockchainDto.POLYGON,
            token = UnionAddress(BlockchainDto.POLYGON, "abc"),
            tokenId = BigInteger("123")
        )

        val serialized = mapper.writeValueAsString(itemId)
        assertEquals(
            "\"POLYGON:abc:123\"",
            serialized
        )

        val deserialized = mapper.readValue(serialized, ItemIdDto::class.java)
        assertEquals(itemId, deserialized)
    }

    @Test
    fun `flow itemId`() {
        val itemId = ItemIdDto(
            blockchain = BlockchainDto.FLOW,
            token = UnionAddress(BlockchainDto.FLOW, "abc"),
            tokenId = BigInteger("123")
        )

        val serialized = mapper.writeValueAsString(itemId)
        assertEquals("\"FLOW:abc:123\"", serialized)

        val deserialized = mapper.readValue(serialized, ItemIdDto::class.java)
        assertEquals(itemId, deserialized)
    }

    @Test
    fun `eth ownershipId`() {
        val itemId = OwnershipIdDto(
            blockchain = BlockchainDto.ETHEREUM,
            token = UnionAddress(BlockchainDto.ETHEREUM, "abc"),
            tokenId = BigInteger("123"),
            owner = UnionAddress(BlockchainDto.ETHEREUM, "xyz")
        )

        val serialized = mapper.writeValueAsString(itemId)
        assertEquals(
            "\"ETHEREUM:abc:123:xyz\"",
            serialized
        )

        val deserialized = mapper.readValue(serialized, OwnershipIdDto::class.java)
        assertEquals(itemId, deserialized)
    }

    @Test
    fun `flow ownershipId`() {
        val ownershipId = OwnershipIdDto(
            blockchain = BlockchainDto.FLOW,
            token = UnionAddress(BlockchainDto.FLOW, "abc"),
            tokenId = BigInteger("123"),
            owner = UnionAddress(BlockchainDto.FLOW, "xyz")
        )

        val serialized = mapper.writeValueAsString(ownershipId)
        assertEquals(
            "\"FLOW:abc:123:xyz\"",
            serialized
        )

        val deserialized = mapper.readValue(serialized, OwnershipIdDto::class.java)
        assertEquals(ownershipId, deserialized)
    }

    @Test
    fun `eth order id`() {
        val ethOrderId = OrderIdDto(BlockchainDto.ETHEREUM, "754")

        val serialized = mapper.writeValueAsString(ethOrderId)
        assertEquals("\"ETHEREUM:754\"", serialized)

        val deserialized = mapper.readValue(serialized, OrderIdDto::class.java)
        assertEquals(ethOrderId, deserialized)
    }

    @Test
    fun `flow order id`() {
        val flowOrderId = OrderIdDto(BlockchainDto.FLOW, "754")

        val serialized = mapper.writeValueAsString(flowOrderId)
        assertEquals("\"FLOW:754\"", serialized)

        val deserialized = mapper.readValue(serialized, OrderIdDto::class.java)
        assertEquals(flowOrderId, deserialized)
    }

    @Test
    fun `eth activity id`() {
        val ethActivityId = ActivityIdDto(BlockchainDto.ETHEREUM, "754")

        val serialized = mapper.writeValueAsString(ethActivityId)
        assertEquals("\"ETHEREUM:754\"", serialized)

        val deserialized = mapper.readValue(serialized, ActivityIdDto::class.java)
        assertEquals(ethActivityId, deserialized)
    }

    @Test
    fun `flow activity id`() {
        val flowActivityId = ActivityIdDto(BlockchainDto.FLOW, "754")

        val serialized = mapper.writeValueAsString(flowActivityId)
        assertEquals("\"FLOW:754\"", serialized)

        val deserialized = mapper.readValue(serialized, ActivityIdDto::class.java)
        assertEquals(flowActivityId, deserialized)
    }

}