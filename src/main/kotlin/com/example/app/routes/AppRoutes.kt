package com.example.app.routes

import com.example.app.exceptions.InvalidInputException
import com.example.app.exceptions.ParsingException
import com.example.app.models.AppRequest
import com.example.app.service.AppService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.http.*


fun Route.appRoutes() {
    val appService = AppService();
        route("/api/benford") {
            post {
                try {
                    val req = call.receive<AppRequest>()
                    if (req.significanceLevel <= 0.0 || req.significanceLevel >= 1.0) {
                        throw InvalidInputException("significanceLevel must be between 0 and 1 exclusive")
                    }

                    val resp = appService.calculateBenfordDistribution(req.text, req.significanceLevel)
                    call.respond(resp)
                } catch (ex: InvalidInputException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to ex.message))
                } catch (ex: ParsingException) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to ex.message))
                }
            }

        }
}