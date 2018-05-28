/*
Copyright 2016 EconomicSL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.economicsl.auctions.multiunit.orders

import java.util.UUID

import org.economicsl.auctions._


trait AskOrder extends GenAskOrder with SinglePricePoint {

  def split(residual: Quantity): (AskOrder, AskOrder)

}


object AskOrder {

  /** By default, instances of `LimitAskOrder` are ordered based on `limit` price from lowest to highest. */
  implicit def ordering[A <: AskOrder]: Ordering[A] = SinglePricePoint.ordering

}


case class LimitAskOrder(issuer: UUID, limit: Price, quantity: Quantity, tradable: Tradable) extends AskOrder {

  def split(residual: Quantity): (LimitAskOrder, LimitAskOrder) = {
    val matched = Quantity(quantity.value - residual.value)
    (this.copy(quantity = matched), this.copy(quantity = residual))
  }

}


case class MarketAskOrder(issuer: UUID, quantity: Quantity, tradable: Tradable) extends AskOrder {

  val limit: Price = Price.MinValue

  def split (residual: Quantity): (MarketAskOrder, MarketAskOrder) = {
    val matched = Quantity(quantity.value - residual.value)
    (this.copy (quantity = matched), this.copy (quantity = residual) )
  }

}
