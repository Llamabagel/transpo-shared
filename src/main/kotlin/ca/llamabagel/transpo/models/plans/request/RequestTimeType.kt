/*
 * Copyright (c) 2018 Llamabagel.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.llamabagel.transpo.models.plans.request

/**
 * Enum describing the type of request being made in relation to the specified time in the request.
 * * ARRIVE_AT The plan should arrive at the destination at the specified time.
 * * DEPART_AT The plan should depart the origin at the specified time.
 */
enum class RequestTimeType {
    ARRIVE_AT,
    DEPART_AT
}