package com.example.anaxa.data.mapper

import com.example.anaxa.data.remote.dto.CategoryDto
import com.example.anaxa.data.remote.dto.GameDto
import com.example.anaxa.data.remote.dto.LotDto
import com.example.anaxa.data.remote.dto.MessageDto
import com.example.anaxa.data.remote.dto.OrderDto
import com.example.anaxa.data.remote.dto.ReviewDto
import com.example.anaxa.data.remote.dto.UserDto
import com.example.anaxa.domain.model.Category
import com.example.anaxa.domain.model.Game
import com.example.anaxa.domain.model.Lot
import com.example.anaxa.domain.model.Message
import com.example.anaxa.domain.model.Order
import com.example.anaxa.domain.model.Review
import com.example.anaxa.domain.model.User

fun UserDto.toDomain() = User(id, email, username, avatarUrl, rating, createdAt)

fun GameDto.toDomain() = Game(id, name, iconUrl, description)

fun CategoryDto.toDomain() = Category(id, gameId, type)

fun LotDto.toDomain() = Lot(
    id = id,
    seller = seller.toDomain(),
    categoryId = categoryId,
    categoryType = categoryType,
    title = title,
    description = description,
    price = price,
    quantity = quantity,
    status = status,
    createdAt = createdAt
)

fun OrderDto.toDomain() = Order(
    id = id,
    lot = lot.toDomain(),
    buyer = buyer.toDomain(),
    seller = seller.toDomain(),
    quantity = quantity,
    status = status,
    createdAt = createdAt
)

fun MessageDto.toDomain() = Message(id, orderId, sender.toDomain(), content, createdAt)

fun ReviewDto.toDomain() = Review(
    id = id,
    orderId = orderId,
    reviewer = reviewer.toDomain(),
    reviewee = reviewee.toDomain(),
    rating = rating,
    comment = comment,
    createdAt = createdAt
)
