package oop.Decorator

import oop.Decorator.base.Noodles
import oop.Decorator.base.SauceDecorator
import oop.Decorator.ingredients.Chicken
import oop.Decorator.ingredients.Peanuts
import oop.Decorator.ingredients.Pork
import oop.Decorator.ingredients.Tuna
import oop.Decorator.noodles.EggNoodles
import oop.Decorator.noodles.UdonNoodles
import oop.Decorator.noodles.WheatNoodles
import oop.Decorator.sauces.BittersweetSauce
import oop.Decorator.sauces.RedPepperSauce
import oop.Decorator.sauces.SateSauce
import oop.Decorator.sauces.TeriyakiSauce
import java.util.*

val scanner = Scanner(System.`in`)

fun main(args : Array<String>) {

  println("##############")
  println("  Little Kai  ")
  println("##############\n")

  val baseNoodles : Noodles = getBaseNoodles(chooseNoodles())
  val noodlesWithIngredient : Noodles = getIngredient(baseNoodles, ::chooseIngredient)
  val noodlesWithIngredientAndSouce : Noodles = getSauce(noodlesWithIngredient, chooseSauce())

  println("> Your order is: ${noodlesWithIngredientAndSouce.calculateCost()} $")
  if (noodlesWithIngredientAndSouce is SauceDecorator) {
    val outOfControl = if (noodlesWithIngredientAndSouce.SPICINESS == 4) "OUT OF CONTROOOL !_!" else ""
    println("> Spiciness is: ${noodlesWithIngredientAndSouce.SPICINESS} out of 4 $outOfControl")
  } else {
    println("> Without sauce")
  }

  println("> Enjoy your noodles! :D")
}

fun chooseNoodles() : Int {
  var noodlesChoice : Int = 0
  while (!(1..3).contains(noodlesChoice)) {
    println("> Choose your noodles!\n")
    println("1> EggNoodles:    3.75 $")
    println("2> UdonNoodles:   4.00 $")
    println("3> WheatNoodles:  3.50 $")
    print("> ")
    noodlesChoice = scanner.nextInt()
  }
  println()
  return noodlesChoice
}

fun getBaseNoodles(choice: Int) : Noodles {
  when (choice) {
    1 -> return EggNoodles()
    2 -> return UdonNoodles()
    else -> return WheatNoodles()
  }
}

fun chooseIngredient(noodles: Noodles) : Int {
  showCart(noodles)
  var ingredientChoice : Int = -1
  while (ingredientChoice != 0 && !(1..4).contains(ingredientChoice)) {
    println("> Choose your ingredient!\n")
    println("1> Chicken: 3.50 $")
    println("2> Peanuts: 2.50 $")
    println("3> Pork:    4.00 $")
    println("4> Tuna:    2.75 $\n")
    println("0> No more ingredients")
    print("> ")
    ingredientChoice = scanner.nextInt()
  }
  println()
  return ingredientChoice
}

fun getIngredient(noodles: Noodles, chooseIngredient: (Noodles) -> Int) : Noodles {
  when (chooseIngredient(noodles)) {
    1 -> return getIngredient(Chicken(noodles), chooseIngredient)
    2 -> return getIngredient(Peanuts(noodles), chooseIngredient)
    3 -> return getIngredient(Pork(noodles), chooseIngredient)
    4 -> return getIngredient(Tuna(noodles), chooseIngredient)
    else -> return noodles
  }
}

private fun showCart(noodles: Noodles) =
  println("> Your cart is: ${noodles.calculateCost()} $")

fun chooseSauce() : Int {
  var sauceChoice : Int = -1
  while (sauceChoice != 0 && !(1..4).contains(sauceChoice)) {
    println("> Choose your sauce!\n")
    println("1> Bittersweet Sauce: 1 out of 4 spiciness")
    println("2> Red Pepper Sauce:  4 out of 4 spiciness")
    println("3> Sate Sauce:        2 out of 4 spiciness")
    println("4> Teriyaki Sauce:    0 out of 4 spiciness\n")
    println("0> No sauce")
    print("> ")
    sauceChoice = scanner.nextInt()
  }
  println()
  return sauceChoice
}

fun getSauce(noodles: Noodles, choice: Int) : Noodles {
  when (choice) {
    1 -> return BittersweetSauce(noodles)
    2 -> return RedPepperSauce(noodles)
    3 -> return SateSauce(noodles)
    4 -> return TeriyakiSauce(noodles)
    else -> return noodles
  }
}
