package com.example.agent.ui.utility

import com.example.agent.data.network.dto.MessageDto
import java.time.Instant

fun MessageDto.toInstant(): Instant =
    Instant.parse(timestamp)
