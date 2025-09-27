package com.example.app.routes

import com.example.app.models.AppRequest
import com.example.app.service.AppService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Route.appRoutes() {
    val appService = AppService();
        route("/api/benford") {
            post {
                val req = call.receive<AppRequest>()

                val resp = appService.calculateBenfordDistribution(req.text, req.significanceLevel)
                call.respond(resp)
            }

        }
}