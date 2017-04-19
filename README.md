# Kotlin OOP and FP Design Patterns

## Index

* OOP
  * [Behavioral Patterns](#behavioral)
    * [ ] [Chain of Responsability](#chain-of-responsability)
    * [x] [Command](#command)
    * [ ] [Interpreter](#interpreter)
    * [ ] [Iterator](#iterator)
    * [ ] [Mediator](#mediator)
    * [ ] [Memento](#memento)
    * [ ] [Null Object](#null-object)
    * [ ] [Observer](#observer)
    * [ ] [State](#state)
    * [ ] [Template](#template)
    * [ ] [Visitor](#visitor)
  * [Creational Patterns](#creational)
    * [ ] [Abstract Factory](#abstract-factory)
    * [ ] [Builder](#builder)
    * [x] [Factory](#factory)
    * [ ] [Object Pool](#object-pool)
    * [ ] [Prototype](#prototype)
    * [x] [Singleton](#singleton)
  * [Structural Patterns](#structural)
    * [ ] [Adapter](#adapter)
    * [ ] [Bridge](#bridge)
    * [x] [Composite](#brigde)
    * [x] [Decorator](#decorator)
    * [x] [Facade](#facade)
    * [ ] [Flyweight](#flyweight)
    * [ ] [Proxy](#proxy)
* FP
  * [Monads](#monads)
    * [x] [Option/Maybe](#option)
    * [ ] Either
    
## Object oriented paradigm

Behavioral
==========

[Chain of Responsability](/src/main/kotlin/oop/ResponsabilityChain)
-----------------------

> It avoids coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. It pass the request along the chain until an object handles it.

This pattern is usually used within a [Composite](#composite)

### Example

```kotlin
interface MessageProcessor {
  fun process(message: Message): String
}

interface UsernameProcessor(val next: MessageProcessor? = null): MessageProcessor {
  override fun process(message: Message): String = when (message) {
    is Message.Username -> message.message.toUpperCase()
    else -> next?.process(message) ?: message.message
  }
}

interface PasswordProcessor(val next: MessageProcessor? = null): MessageProcessor {
  override fun process(message: Message): String = when (message) {
    is Message.Password -> message.message.map { '*' }.joinToString(separator = "")
    else -> next?.process(message) ?: message.message
  }
}

interface PlainTextProcessor(val next: MessageProcessor? = null): MessageProcessor {
  override fun process(message: Message): String = when (message) {
    is Message.PlainText -> message.message
    else -> next?.process(message) ?: message.message
  }
}
```

### Usage

```kotlin
object ProcessingComposite : MessageProcessor {
  val bottom = PlainTextProcessor()
  val next = PasswordProcessor(bottom)
  val usernameProcessor = UsernameProcessor(next)
  
  override fun process(message: Message): String = usernameProcessor.process(message)
}

val password = Message.Password("SuperSecret")
val username = Message.Username("@Cotel")

println(ProcessingComposite.process(password))    // "***********"
println(ProcessingComposite.process(username))    // "@COTEL"
```

[Command](/src/main/kotlin/oop/Command)
-------

> It is an _object-oriented_ callback. Encapsulates a request as an object.
> It decouples the object that invokes the operation from the one that knows how to perform it.

### Example

```kotlin
interface Command {
  fun matches(command: String): Boolean
  fun execute()
}

class ChooseNoodlesCommand(val cart: Cart) : Command {
  companion object {
    val CHOOSE_NOODLES : String = "1"
  }
  
  override fun matches(command: String): Boolean = command == CHOOSE_NOODLES
  
  override fun execute() {
    cart.chooseNoodles()
  }
}

class Processor(val commands: List<Command>,
                val help: Command) {
                
  fun process(command: String): Command = 
    commands.filter { it.matches(command) }.getOrElse(0) { help } 
    
}
```

### Usage

```kotlin
val scanner = Scanner(System.`in`)
val cart = Cart(scanner)
val commands = listOf(
  ChooseNoodlesCommand(cart),
  // ...
)
val proc = Proccessor(commands, CommandNotFound())

var commandChoice = -1
do {

  commandChoice = scanner.nextInt()
  proc.proccess("$commandChoice").execute()
  
} while (commandChoice != 0)

```

Interpreter
----------

> Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language

**In progress**

Iterator
--------

> It provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation

**In progress**

Mediator
---------

> Define an object that encapsulates how a set of objects interact. It designs an intermediary to decouple many peers.

**In progress**

Memento
---------

> It captures and externalizes an object's internal state so it can get back to this state later without violating encapsulation

**In progress**

Null Object
-----------

> It encapsulates the absence of an object by providing an alternative that offers suitable default behaviour for doing nothing.
> Useful to abstract the handling of null away from the client

**In progress**

Observer
------------

> It defines a _one-to-many_ dependency between object so that when one changes its state, all its dependents are notified and updated automatically.

**In progress**

State
-------

> It allows an object to alter its behaviour when its internal state changes.

**In progress**

[Strategy](/src/main/kotlin/oop/Strategy)
---------

> Define a family of algorithms, encapsulate each one, and make them interchangeable. It captures the abstraction in an interface and buries implementation details in derived classes.

In Kotlin, we can implement this pattern with only functions because of the support for first order functions.

### Example

```kotlin
val GeneralStrategy : (Double) -> Double = { it + it * 0.21 }

val ReducedStrategy : (Double) -> Double = { it + it * 0.10 }

val SuperReducedStrategy = { cost: Double -> cost + cost * 0.04 } // Alternative way to define it

```

### Usage

```kotlin
var ivaStrategy = GeneralStrategy  // You can change the strategy in execution time
val price = 3.14

println(ivaStrategy(price))

```

Template
---------

> Define an skeleton of an algorithm in an operation, deferring some steps to client subclasses. These subclasses can redefine certain steps of an algorightm without changing its structure

**In progress**

Visitor
------

> Represent an operation to be performed on the elements of an object structure. It lets you define a new operation without changing the classes of the elements on which it operates.

**In progress**

Creational
==========

Abstract Factory
----------------

> It provides an interface for creating families of related dependents objects without specifying their concrete classes

**In progress**

Builder
---------

> It separates the construction of a complex object from its representation so that the same construction process can create different representations.

**In progress**

[Factory](/src/main/kotlin/oop/Factory)
--------

> It defines an interface for creating an object, but let subclasses decide which class to instantiate.

### Example
```kotlin
class NoodlesFactory {
  fun getNoodles(noodleType: Int): Noodles {
    when (noodleType) {
      1 -> return EggNoodles()
      2 -> return UdonNoodles()
      3 -> return WheatNoodles()
      else -> throw NoNoodlesMatchException()
    }
  }
}

```

### Usage

```kotlin
val scanner = Scanner(System.`in`)
val noodlesFactory = NoodlesFactory()

val noodlesType = scanner.nextInt()
val noodles = noodlesFactory.getNoodles(noodlesType)
```

Object Pool
-----------

> An Object Pool can offer a significant performance boost, it is most effective in situations where the cost of initializing a class instance is high.

**In progress**

Prototype
-----------

> It specifies the kind of objects to create using a prototypical instance and create new objects by copying this instance.

**In progress**

Singleton
---------

> It ensures a class has only one instance and provide a global point of access to it.

In Kotlin we can make use of the reserved keyword `Object`

### Example

```kotlin
object OneInstance {

  fun sayHello() = println("Hello")

}

```

### Usage

```kotlin
OneInstance.sayHello()
```

Structural
==========

Adapter
--------

> It converts an interface of a class into another interface clients expect. It lets classes work together that couldn't otherwise.

**In progress**

Bridge
------

> It decouples an abstraction from its implementation so that the two can vary independently.

**In progress**

[Composite](/src/main/kotlin/oop/Composite)
--------

> Compose objects into tress structures to represent whole-part hierarchies. It lets clients treat individual objects and compositions uniformly.

### Example

```kotlin
interface Cooker {
  fun cook()
}

interface ChineseCooker(private val cookers: MutableList<Cooker> = mutableListOf()): Cooker {
  fun add(cooker: Cooker) = cookers.add(cooker)
  
  override fun cook() {
    println("Cooking plate with soy sauce")
    cookers.forEach(Cooker::cook)
  }
}

interface DeepFryer(): Cooker {
  override fun cook() {
    println("And frying")
  }
}

class Kitchen(private val cookers: MutableList<Cooker> = mutableListOf()): Cooker {
  fun add(cooker: Cooker) {
    cookers.add(cooker)
  }
  
  fun remove(cooker: Cooker) {
    cookers.remove(cooker)
  }
  
  override fun cook() {
    cookers.forEach(Cooker::cook)
  }
}

```

### Usage

```kotlin
val chinese = ChineseCooker()
chinese.add(DeepFryer())

val cookers = mutableListOf(chinese)
val kitchen = Kitchen(cookers)
kitchen.cook()
```

[Decorator](/src/main/kotlin/oop/Decorator)
----------

> It attachs additional responsabilities to an object dynamically.

In Kotlin, we don't need to redefine the methods of the decorated interface. We can use `by` to delegate those methods to the decorated class.

### Example

```kotlin
interface Noodles {
  fun calculateCost(): Double
}

class UdonNoodles : Noodles {
  override fun calculateCost() = 3.50
}

abstract class SauceDecorator(private val noodles: Noodles): Noodles by noodles {
  abstract val SPICINESS: Int
}

class RedPepperSauce(noodles: Noodles): SauceDecorator(noodles) {
  override val SPICINESS: Int = 3
}
```
### Usage

```kotlin
val udonNoodles = UdonNoodles()
val udonNoodlesWithRedPepperSauce = RedPepperSauce(udonNoodles)

println(udonNoodlesWithRedPepperSauce.calculateCost()) // 3.50
println(udonNoodlesWithRedPepperSauce.SPICINESS) // 3
```

[Facade](/src/main/kotlin/oop/Facade)
----------

> It provides a unified interface to a set of interfaces in a subsystem.

### Example

```kotlin
class NetInvoiceSalaryFacade(val iva: IVAOperation = IVAOperation(),
                             val irpf: IRPFOperation = IRPFOperation()) {
  fun calculate(salary: Double): Double =
    salary + iva.apply(salary) - irpf.apply(salary)
    
  fun calculateAnnual(monthlySalary: Double): Double = 
    calculate(monthlySalary * 12)
}

class IVAOperation {
  fun apply(amount: Double): Double = amount + (amount * 0.21)
}

class IRPFOperation {
  fun apply(amount: Double): Double = amount + (amount * 0.15)
}
```

### Usage

```kotlin
val facade = NetInvoiceSalaryFacade()
println(facade.calculateAnnual(1000))
```

Flyweight
----------

> It uses sharing to support large numbers of fine-grained objects efficiently

**In progress**

Proxy
------

> It provides a placeholder for another object to control access to it.
> It is an extra level of indirection to support controlled or intelligent access.

**In progress**

## Functional paradigm

Monads
========

[Option](/src/main/kotlin/fp/Maybe) AKA Maybe
------

> It represents a value which is of type _A_ or none.

### Example

```kotlin
sealed class Maybe<A> {
  companion object {
    fun <A: Any> fromNullable(a: A?): Maybe<A> = if (a != null) Maybe.Just(a) else Maybe.None
  }
  
  abstract val isEmpty: Boolean
  
  fun <B> fold(ifEmpty: () -> B, fn: (A) -> B): B = when (this) {
    is Maybe.None -> ifEmpty()
    is Maybe.Just -> fn(value)
  }
}

fun <B> Maybe<B>.getOrElse(default: () -> B): B = fold({ default() }, { it })
```

### Usage

```kotlin
val returnExplanatoryString: () -> String = { "Result is null" }
val listOfCountries = listOf("Spain", "France", "Italy")
val getValueInList: (String) -> (List<String>) -> String? = { item -> 
  {
    list -> list.find { it == item }
  }
}
val getTanzaniaInList = getValueInList("Tanzania")

val maybeTanzania = Maybe.fromNullable(getTanzaniaInList(listOfCountries))

val country = maybeTanzania
  .map(String::toUpperCase)
  .getOrElse(returnExplanatoryString)
val countryClassic = listOfCountries.find { it == "Tanzania" }?.toUpperCase()

println(country)        // This won't be null
println(countryClassic) // This can be null

```


